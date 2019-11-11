package galdr.processor;

import java.io.IOException;
import java.util.List;
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
  String getRootAnnotationClassname()
  {
    return Constants.SUB_SYSTEM_CLASSNAME;
  }

  @Override
  final void process( @Nonnull final TypeElement element )
    throws IOException, ProcessorException
  {
    if ( ElementKind.CLASS != element.getKind() )
    {
      throw new ProcessorException( "@SubSystem target must be a class", element );
    }
    else if ( !element.getModifiers().contains( Modifier.ABSTRACT ) )
    {
      throw new ProcessorException( "@SubSystem target must be abstract", element );
    }
    else if ( NestingKind.TOP_LEVEL != element.getNestingKind() && !element.getModifiers().contains( Modifier.STATIC ) )
    {
      throw new ProcessorException( "@SubSystem target must not be a non-static nested class", element );
    }
    final AnnotationMirror annotation = ProcessorUtil.getAnnotationByType( element, Constants.SUB_SYSTEM_CLASSNAME );
    final String name = deriveName( element, annotation );
    final int priority = ProcessorUtil.getAnnotationValue( annotation, "priority" );
    final SubSystemDescriptor descriptor = new SubSystemDescriptor( element, name, priority );

    final List<ExecutableElement> methods =
      ProcessorUtil.getMethods( element, processingEnv.getElementUtils(), processingEnv.getTypeUtils() );
    for ( final ExecutableElement method : methods )
    {
      final AnnotationMirror nameRef = ProcessorUtil.findAnnotationByType( method, Constants.NAME_REF_CLASSNAME );
      final AnnotationMirror worldRef = ProcessorUtil.findAnnotationByType( method, Constants.WORLD_REF_CLASSNAME );

      if ( null != nameRef )
      {
        addNameRef( descriptor, method );
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

  private void addNameRef( @Nonnull final SubSystemDescriptor descriptor, @Nonnull final ExecutableElement method )
  {
    MemberChecks.mustBeAbstract( Constants.NAME_REF_CLASSNAME, method );
    MemberChecks.mustNotBePackageAccessInDifferentPackage( descriptor.getElement(),
                                                           Constants.APPLICATION_CLASSNAME,
                                                           Constants.NAME_REF_CLASSNAME,
                                                           method );
    MemberChecks.mustNotHaveAnyParameters( Constants.NAME_REF_CLASSNAME, method );
    MemberChecks.mustReturnAValue( Constants.NAME_REF_CLASSNAME, method );
    MemberChecks.mustNotThrowAnyExceptions( Constants.NAME_REF_CLASSNAME, method );

    final TypeMirror returnType = method.getReturnType();
    if ( TypeKind.DECLARED != returnType.getKind() || !String.class.getName().equals( returnType.toString() ) )
    {
      throw new ProcessorException( "Method annotated with @NameRef must return an instance of java.lang.String",
                                    method );
    }
    descriptor.addNameRef( method );
  }

  private void addWorldRef( @Nonnull final SubSystemDescriptor descriptor, @Nonnull final ExecutableElement method )
  {
    MemberChecks.mustBeAbstract( Constants.WORLD_REF_CLASSNAME, method );
    MemberChecks.mustNotBePackageAccessInDifferentPackage( descriptor.getElement(),
                                                           Constants.APPLICATION_CLASSNAME,
                                                           Constants.WORLD_REF_CLASSNAME,
                                                           method );
    MemberChecks.mustNotHaveAnyParameters( Constants.WORLD_REF_CLASSNAME, method );
    MemberChecks.mustReturnAValue( Constants.WORLD_REF_CLASSNAME, method );
    MemberChecks.mustNotThrowAnyExceptions( Constants.WORLD_REF_CLASSNAME, method );

    final TypeMirror returnType = method.getReturnType();
    if ( TypeKind.DECLARED != returnType.getKind() || !Constants.WORLD_CLASSNAME.equals( returnType.toString() ) )
    {
      throw new ProcessorException( "Method annotated with @WorldRef must return an instance of galdr.World",
                                    method );
    }
    descriptor.addWorldRef( method );
  }

  @Nonnull
  private String deriveName( @Nonnull final TypeElement element, @Nonnull final AnnotationMirror annotation )
    throws ProcessorException
  {
    final String name = ProcessorUtil.getAnnotationValue( annotation, "name" );
    return Constants.SENTINEL_NAME.equals( name ) ? element.getSimpleName().toString() : name;
  }
}
