package galdr.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

/**
 * Annotation processor that generates subsystem implementations.
 */
@SupportedAnnotationTypes( Constants.SUB_SYSTEM_CLASSNAME )
@SupportedSourceVersion( SourceVersion.RELEASE_8 )
@SupportedOptions( { "galdr.defer.errors", "galdr.defer.unresolved" } )
public final class SubSystemProcessor
  extends AbstractGaldrProcessor
{
  @Override
  @Nonnull
  protected String getRootAnnotationClassname()
  {
    return Constants.SUB_SYSTEM_CLASSNAME;
  }

  @Override
  protected final void process( @Nonnull final TypeElement element )
    throws IOException, ProcessorException
  {
    if ( ElementKind.CLASS != element.getKind() )
    {
      throw new ProcessorException( MemberChecks.must( Constants.SUB_SYSTEM_CLASSNAME, "be a class" ), element );
    }
    else if ( !element.getModifiers().contains( Modifier.ABSTRACT ) )
    {
      throw new ProcessorException( MemberChecks.must( Constants.SUB_SYSTEM_CLASSNAME, "be abstract" ), element );
    }
    else if ( NestingKind.TOP_LEVEL != element.getNestingKind() && !element.getModifiers().contains( Modifier.STATIC ) )
    {
      final String message = MemberChecks.mustNot( Constants.SUB_SYSTEM_CLASSNAME, "be a non-static nested class" );
      throw new ProcessorException( message, element );
    }
    final AnnotationMirror annotation = AnnotationsUtil.getAnnotationByType( element, Constants.SUB_SYSTEM_CLASSNAME );
    final String name = deriveName( element, annotation );
    final SubSystemDescriptor descriptor = new SubSystemDescriptor( element, name );

    final List<ExecutableElement> constructors = ProcessorUtil.getConstructors( element );
    if ( constructors.size() > 1 )
    {
      final String message = MemberChecks.must( Constants.SUB_SYSTEM_CLASSNAME, "have no more than one constructor" );
      throw new ProcessorException( message, element );
    }
    else if ( !constructors.isEmpty() && constructors.get( 0 ).getModifiers().contains( Modifier.PRIVATE ) )
    {
      final String message = MemberChecks.mustNot( Constants.SUB_SYSTEM_CLASSNAME, "have a private constructor" );
      throw new ProcessorException( message, element );
    }

    final List<ExecutableElement> methods =
      ProcessorUtil.getMethods( element, processingEnv.getElementUtils(), processingEnv.getTypeUtils() );
    for ( final ExecutableElement method : methods )
    {
      final AnnotationMirror componentManagerRef =
        AnnotationsUtil.findAnnotationByType( method, Constants.COMPONENT_MANAGER_REF_CLASSNAME );
      final AnnotationMirror entityProcessor =
        AnnotationsUtil.findAnnotationByType( method, Constants.ENTITY_PROCESSOR_CLASSNAME );
      final AnnotationMirror nameRef = AnnotationsUtil.findAnnotationByType( method, Constants.NAME_REF_CLASSNAME );
      final AnnotationMirror onActivate =
        AnnotationsUtil.findAnnotationByType( method, Constants.ON_ACTIVATE_CLASSNAME );
      final AnnotationMirror onDeactivate =
        AnnotationsUtil.findAnnotationByType( method, Constants.ON_DEACTIVATE_CLASSNAME );
      final AnnotationMirror processor = AnnotationsUtil.findAnnotationByType( method, Constants.PROCESSOR_CLASSNAME );
      final AnnotationMirror worldRef = AnnotationsUtil.findAnnotationByType( method, Constants.WORLD_REF_CLASSNAME );

      if ( null != componentManagerRef )
      {
        addComponentManagerRef( descriptor, method );
      }
      else if ( null != entityProcessor )
      {
        addEntityProcessor( descriptor, method );
      }
      else if ( null != nameRef )
      {
        addNameRef( descriptor, method );
      }
      else if ( null != onActivate )
      {
        addOnActivate( descriptor, method );
      }
      else if ( null != onDeactivate )
      {
        addOnDeactivate( descriptor, method );
      }
      else if ( null != processor )
      {
        addProcessor( descriptor, method );
      }
      else if ( null != worldRef )
      {
        addWorldRef( descriptor, method );
      }
    }

    GeneratorUtil.emitJavaType( descriptor.getPackageName(),
                                Generator.buildSubSystem( processingEnv, descriptor ),
                                processingEnv.getFiler() );
  }

  private void addComponentManagerRef( @Nonnull final SubSystemDescriptor descriptor,
                                       @Nonnull final ExecutableElement method )
  {
    mustBeRefMethod( descriptor, method, Constants.COMPONENT_MANAGER_REF_CLASSNAME );

    final TypeMirror returnType = method.getReturnType();
    final TypeName typeName = TypeName.get( returnType );
    if ( !( typeName instanceof ParameterizedTypeName ) )
    {
      throw newBadBadComponentManagerRefTypeException( method );
    }
    final ParameterizedTypeName parameterizedTypeName = (ParameterizedTypeName) typeName;
    if ( !parameterizedTypeName.rawType.toString().equals( Constants.COMPONENT_MANAGER_CLASSNAME ) )
    {
      throw newBadBadComponentManagerRefTypeException( method );
    }
    final TypeName typeArgument = parameterizedTypeName.typeArguments.get( 0 );
    if ( !( typeArgument instanceof ClassName ) )
    {
      throw newBadBadComponentManagerRefTypeException( method );
    }
    final ClassName componentType = (ClassName) typeArgument;
    descriptor.addComponentManagerRef( method, componentType );
  }

  @Nonnull
  private ProcessorException newBadBadComponentManagerRefTypeException( @Nonnull final ExecutableElement method )
  {
    return new ProcessorException( "@ComponentManagerRef target must return the type " +
                                   Constants.COMPONENT_MANAGER_CLASSNAME + " parameterized with the desired " +
                                   "component type", method );
  }

  private void addEntityProcessor( @Nonnull final SubSystemDescriptor descriptor,
                                   @Nonnull final ExecutableElement method )
  {
    mustBeLifecycleMethod( descriptor, method, Constants.ENTITY_PROCESSOR_CLASSNAME );
    final List<? extends VariableElement> parameters = method.getParameters();
    if ( parameters.isEmpty() ||
         parameters.size() > 2 ||
         parameters.stream().anyMatch( p -> TypeKind.INT != p.asType().getKind() ) )
    {
      throw new ProcessorException( MemberChecks.must( Constants.ENTITY_PROCESSOR_CLASSNAME,
                                                       "have one or two integer parameters" ),
                                    method );
    }
    final List<TypeName> all = getTypeNameParameterValue( method, "all" );
    final List<TypeName> one = getTypeNameParameterValue( method, "one" );
    final List<TypeName> exclude = getTypeNameParameterValue( method, "exclude" );

    for ( final TypeName candidate : all )
    {
      for ( final TypeName a : all )
      {
        if ( a != candidate && a.equals( candidate ) )
        {
          throw new ProcessorException( "@EntityProcessor target contains the component of type " + candidate +
                                        " multiple times in the 'all' requirement", method );
        }
      }
      if ( one.contains( candidate ) )
      {
        throw new ProcessorException( "@EntityProcessor target contains the component of type " + candidate +
                                      " in the 'all' requirement and the 'one' requirement", method );
      }
      if ( exclude.contains( candidate ) )
      {
        throw new ProcessorException( "@EntityProcessor target contains the component of type " + candidate +
                                      " in the 'all' requirement and the 'exclude' requirement", method );
      }
    }
    for ( final TypeName candidate : one )
    {
      for ( final TypeName a : one )
      {
        if ( a != candidate && a.equals( candidate ) )
        {
          throw new ProcessorException( "@EntityProcessor target contains the component of type " + candidate +
                                        " multiple times in the 'one' requirement", method );
        }
      }
      if ( exclude.contains( candidate ) )
      {
        throw new ProcessorException( "@EntityProcessor target contains the component of type " + candidate +
                                      " in the 'one' requirement and the 'exclude' requirement", method );
      }
    }

    for ( final TypeName candidate : exclude )
    {
      for ( final TypeName a : exclude )
      {
        if ( a != candidate && a.equals( candidate ) )
        {
          throw new ProcessorException( "@EntityProcessor target contains the component of type " + candidate +
                                        " multiple times in the 'exclude' requirement", method );
        }
      }
    }
    if ( all.isEmpty() && one.isEmpty() && exclude.isEmpty() )
    {
      throw new ProcessorException( "@EntityProcessor target must specify at least one component in the 'all', " +
                                    "'one' or 'exclude' requirements", method );
    }
    if ( 1 == one.size() )
    {
      throw new ProcessorException( "@EntityProcessor target must have multiple components in the 'one' requirement " +
                                    "or alternatively the component should be moved to the 'all' requirement", method );
    }
    descriptor.addEntityProcessor( method, all, one, exclude );
  }

  @Nonnull
  private List<TypeName> getTypeNameParameterValue( @Nonnull final ExecutableElement method,
                                                    @Nonnull final String parameterName )
  {
    return AnnotationsUtil.getTypeMirrorsAnnotationParameter( method,
                                                              Constants.ENTITY_PROCESSOR_CLASSNAME,
                                                              parameterName )
      .stream().map( TypeName::get ).collect( Collectors.toList() );
  }

  private void addNameRef( @Nonnull final SubSystemDescriptor descriptor, @Nonnull final ExecutableElement method )
  {
    mustBeRefMethod( descriptor, method, Constants.NAME_REF_CLASSNAME );
    MemberChecks.mustReturnAnInstanceOf( processingEnv, method, Constants.NAME_REF_CLASSNAME, String.class.getName() );
    descriptor.addNameRef( method );
  }

  private void addOnActivate( @Nonnull final SubSystemDescriptor descriptor, @Nonnull final ExecutableElement method )
  {
    mustBeLifecycleMethod( descriptor, method, Constants.ON_ACTIVATE_CLASSNAME );
    MemberChecks.mustNotHaveAnyParameters( Constants.ON_ACTIVATE_CLASSNAME, method );
    descriptor.addOnActivate( method );
  }

  private void addOnDeactivate( @Nonnull final SubSystemDescriptor descriptor, @Nonnull final ExecutableElement method )
  {
    mustBeLifecycleMethod( descriptor, method, Constants.ON_DEACTIVATE_CLASSNAME );
    MemberChecks.mustNotHaveAnyParameters( Constants.ON_DEACTIVATE_CLASSNAME, method );
    descriptor.addOnDeactivate( method );
  }

  private void addProcessor( @Nonnull final SubSystemDescriptor descriptor, @Nonnull final ExecutableElement method )
  {
    mustBeLifecycleMethod( descriptor, method, Constants.PROCESSOR_CLASSNAME );
    final List<? extends VariableElement> parameters = method.getParameters();
    if ( parameters.size() > 1 || ( 1 == parameters.size() && TypeKind.INT != parameters.get( 0 ).asType().getKind() ) )
    {
      throw new ProcessorException( MemberChecks.must( Constants.PROCESSOR_CLASSNAME,
                                                       "have zero parameters or a single integer parameter" ), method );
    }
    descriptor.addProcessor( method );
  }

  private void addWorldRef( @Nonnull final SubSystemDescriptor descriptor, @Nonnull final ExecutableElement method )
  {
    mustBeRefMethod( descriptor, method, Constants.WORLD_REF_CLASSNAME );
    MemberChecks.mustReturnAnInstanceOf( processingEnv,
                                         method,
                                         Constants.WORLD_REF_CLASSNAME,
                                         Constants.WORLD_CLASSNAME );
    descriptor.addWorldRef( method );
  }

  private void mustBeLifecycleMethod( @Nonnull final SubSystemDescriptor descriptor,
                                      @Nonnull final ExecutableElement method,
                                      @Nonnull final String annotationClassname )
  {
    MemberChecks.mustNotBeAbstract( annotationClassname, method );
    MemberChecks.mustNotBePrivate( annotationClassname, method );
    MemberChecks.mustNotBeStatic( annotationClassname, method );
    final TypeElement typeElement = descriptor.getElement();
    MemberChecks.mustNotBePackageAccessInDifferentPackage( typeElement,
                                                           Constants.SUB_SYSTEM_CLASSNAME,
                                                           annotationClassname,
                                                           method );
    MemberChecks.mustNotReturnAnyValue( annotationClassname, method );
    MemberChecks.mustNotThrowAnyExceptions( annotationClassname, method );
    MemberChecks.mustBeInternalMethod( processingEnv,
                                       typeElement,
                                       method,
                                       annotationClassname,
                                       Constants.WARNING_PUBLIC_LIFECYCLE_METHOD,
                                       Constants.WARNING_PROTECTED_LIFECYCLE_METHOD,
                                       Constants.SUPPRESS_GALDR_WARNINGS_ANNOTATION_CLASSNAME );
  }

  private void mustBeRefMethod( @Nonnull final SubSystemDescriptor descriptor,
                                @Nonnull final ExecutableElement method,
                                @Nonnull final String annotationClassname )
  {
    MemberChecks.mustBeAbstract( annotationClassname, method );
    final TypeElement typeElement = descriptor.getElement();
    MemberChecks.mustNotBePackageAccessInDifferentPackage( typeElement,
                                                           Constants.SUB_SYSTEM_CLASSNAME,
                                                           annotationClassname,
                                                           method );
    MemberChecks.mustNotHaveAnyParameters( annotationClassname, method );
    MemberChecks.mustReturnAValue( annotationClassname, method );
    MemberChecks.mustNotThrowAnyExceptions( annotationClassname, method );

    MemberChecks.mustBeInternalMethod( processingEnv,
                                       typeElement,
                                       method,
                                       annotationClassname,
                                       Constants.WARNING_PUBLIC_REF_METHOD,
                                       Constants.WARNING_PROTECTED_REF_METHOD,
                                       Constants.SUPPRESS_GALDR_WARNINGS_ANNOTATION_CLASSNAME );
  }

  @Nonnull
  private String deriveName( @Nonnull final TypeElement element, @Nonnull final AnnotationMirror annotation )
    throws ProcessorException
  {
    final String name = AnnotationsUtil.getAnnotationValue( annotation, "name" );
    return Constants.SENTINEL_NAME.equals( name ) ? element.getSimpleName().toString() : name;
  }
}
