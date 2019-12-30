package galdr.processor;

import com.squareup.javapoet.ClassName;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.processing.RoundEnvironment;
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
import javax.lang.model.type.TypeMirror;
import org.realityforge.proton.AnnotationsUtil;
import org.realityforge.proton.ElementsUtil;
import org.realityforge.proton.MemberChecks;
import org.realityforge.proton.ProcessorException;

/**
 * Annotation processor that generates application implementations.
 */
@SupportedAnnotationTypes( Constants.GALDR_APPLICATION_CLASSNAME )
@SupportedSourceVersion( SourceVersion.RELEASE_8 )
@SupportedOptions( { "galdr.defer.unresolved", "galdr.defer.errors" } )
public final class ApplicationProcessor
  extends AbstractGaldrProcessor
{
  @SuppressWarnings( "unchecked" )
  @Override
  public boolean process( @Nonnull final Set<? extends TypeElement> annotations, @Nonnull final RoundEnvironment env )
  {
    final TypeElement annotation =
      processingEnv.getElementUtils().getTypeElement( Constants.GALDR_APPLICATION_CLASSNAME );
    final Collection<TypeElement> elements = (Collection<TypeElement>) env.getElementsAnnotatedWith( annotation );
    processTypeElements( env, elements, this::process );
    errorIfProcessingOverAndInvalidTypesDetected( env );
    return true;
  }

  private void process( @Nonnull final TypeElement element )
    throws IOException, ProcessorException
  {
    if ( ElementKind.CLASS != element.getKind() )
    {
      throw new ProcessorException( MemberChecks.must( Constants.GALDR_APPLICATION_CLASSNAME, "be a class" ), element );
    }
    else if ( !element.getModifiers().contains( Modifier.ABSTRACT ) )
    {
      throw new ProcessorException( MemberChecks.must( Constants.GALDR_APPLICATION_CLASSNAME, "be abstract" ),
                                    element );
    }
    else if ( NestingKind.TOP_LEVEL != element.getNestingKind() && !element.getModifiers().contains( Modifier.STATIC ) )
    {
      final String message =
        MemberChecks.mustNot( Constants.GALDR_APPLICATION_CLASSNAME, "be a non-static nested class" );
      throw new ProcessorException( message, element );
    }
    final ApplicationDescriptor descriptor = new ApplicationDescriptor( element );

    final List<TypeMirror> components =
      AnnotationsUtil.getTypeMirrorsAnnotationParameter( element, Constants.GALDR_APPLICATION_CLASSNAME, "components" );
    final List<ClassName> componentTypes = new ArrayList<>();
    for ( final TypeMirror component : components )
    {
      final TypeElement componentType = (TypeElement) processingEnv.getTypeUtils().asElement( component );
      if ( null == componentType ||
           !AnnotationsUtil.hasAnnotationOfType( componentType, Constants.COMPONENT_CLASSNAME ) )
      {
        throw new ProcessorException( MemberChecks.must( Constants.GALDR_APPLICATION_CLASSNAME,
                                                         "have a components parameter that references classes annotated by " +
                                                         MemberChecks.toSimpleName( Constants.COMPONENT_CLASSNAME ) +
                                                         " but references the type " + component + " that is not " +
                                                         "annotated appropriately" ),
                                      element );
      }
      final ClassName className = ClassName.get( componentType );
      if ( componentTypes.contains( className ) )
      {
        throw new ProcessorException( MemberChecks.mustNot( Constants.GALDR_APPLICATION_CLASSNAME,
                                                            "have a duplicate type in the components " +
                                                            "parameter. The type " + component + " appears " +
                                                            "multiple times" ),
                                      element );
      }
      componentTypes.add( className );
      final VariableElement parameter = (VariableElement)
        AnnotationsUtil
          .getAnnotationValue( componentType, Constants.COMPONENT_CLASSNAME, "storage" )
          .getValue();
      final String storageName = parameter.getSimpleName().toString();
      final StorageType storageType =
        "AUTODETECT".equals( storageName ) ?
        autoDetectComponentStorage( componentType ) :
        StorageType.valueOf( storageName );
      descriptor.addComponent( new ComponentDescriptor( component, storageType ) );
    }
    if ( componentTypes.isEmpty() )
    {
      throw new ProcessorException( MemberChecks.must( Constants.GALDR_APPLICATION_CLASSNAME,
                                                       "have at least one Component defined by the components parameter" ),
                                    element );
    }
    final List<ExecutableElement> constructors = ElementsUtil.getConstructors( element );
    if ( constructors.size() > 1 )
    {
      final String message =
        MemberChecks.must( Constants.GALDR_APPLICATION_CLASSNAME, "have no more than one constructor" );
      throw new ProcessorException( message, element );
    }
    else if ( !constructors.isEmpty() && !constructors.get( 0 ).getParameters().isEmpty() )
    {
      final String message =
        MemberChecks.must( Constants.GALDR_APPLICATION_CLASSNAME, "have a constructor with no parameters" );
      throw new ProcessorException( message, constructors.get( 0 ) );
    }
    else if ( !constructors.isEmpty() && constructors.get( 0 ).getModifiers().contains( Modifier.PRIVATE ) )
    {
      final String message =
        MemberChecks.mustNot( Constants.GALDR_APPLICATION_CLASSNAME, "have a private constructor" );
      throw new ProcessorException( message, constructors.get( 0 ) );
    }

    final List<ExecutableElement> methods =
      ElementsUtil.getMethods( element, processingEnv.getElementUtils(), processingEnv.getTypeUtils() );
    for ( final ExecutableElement method : methods )
    {
      final AnnotationMirror stage = AnnotationsUtil.findAnnotationByType( method, Constants.GALDR_STAGE_CLASSNAME );

      if ( null != stage )
      {
        addStage( descriptor, method );
      }
    }

    emitTypeSpec( descriptor.getPackageName(), Generator.buildApplication( processingEnv, descriptor ) );
  }

  @Nonnull
  private StorageType autoDetectComponentStorage( @Nonnull final TypeElement componentType )
  {
    final List<VariableElement> fields = ElementsUtil.getFields( componentType );
    return fields.isEmpty() ? StorageType.NONE : StorageType.ARRAY;
  }

  private void addStage( @Nonnull final ApplicationDescriptor descriptor, @Nonnull final ExecutableElement method )
  {
    MemberChecks.mustBeAbstract( Constants.GALDR_STAGE_CLASSNAME, method );
    final TypeElement typeElement = descriptor.getElement();
    MemberChecks.mustNotBePackageAccessInDifferentPackage( typeElement,
                                                           Constants.GALDR_APPLICATION_CLASSNAME,
                                                           Constants.GALDR_STAGE_CLASSNAME,
                                                           method );
    MemberChecks.mustNotHaveAnyParameters( Constants.GALDR_STAGE_CLASSNAME, method );
    MemberChecks.mustReturnAValue( Constants.GALDR_STAGE_CLASSNAME, method );
    MemberChecks.mustNotThrowAnyExceptions( Constants.GALDR_STAGE_CLASSNAME, method );
    MemberChecks.mustReturnAnInstanceOf( processingEnv,
                                         method,
                                         Constants.GALDR_STAGE_CLASSNAME,
                                         Constants.STAGE_CLASSNAME );
    final String name =
      AnnotationsUtil.extractName( method,
                                   m -> method.getSimpleName().toString(),
                                   Constants.GALDR_STAGE_CLASSNAME,
                                   "name",
                                   Constants.SENTINEL_NAME );
    for ( final StageDescriptor other : descriptor.getStages() )
    {
      if ( other.getName().equals( name ) )
      {
        throw new ProcessorException( MemberChecks.mustNot( Constants.GALDR_STAGE_CLASSNAME,
                                                            "have the same name as any other stage but " +
                                                            "the stage defined by the method named " +
                                                            other.getMethod().getSimpleName().toString() + " has " +
                                                            "the same name" ),
                                      method );
      }
    }
    final List<TypeMirror> subSystems =
      AnnotationsUtil.getTypeMirrorsAnnotationParameter( method, Constants.GALDR_STAGE_CLASSNAME, "value" );

    final List<ClassName> subSystemTypes = new ArrayList<>();
    for ( final TypeMirror subSystem : subSystems )
    {
      final TypeElement subSystemType = (TypeElement) processingEnv.getTypeUtils().asElement( subSystem );
      if ( null == subSystemType ||
           !AnnotationsUtil.hasAnnotationOfType( subSystemType, Constants.SUB_SYSTEM_CLASSNAME ) )
      {
        throw new ProcessorException( MemberChecks.must( Constants.GALDR_STAGE_CLASSNAME,
                                                         "have a value parameter that references classes annotated by " +
                                                         MemberChecks.toSimpleName( Constants.SUB_SYSTEM_CLASSNAME ) +
                                                         " but references the type " + subSystem + " that is not " +
                                                         "annotated appropriately" ),
                                      method );
      }
      final ClassName className = Generator.toGeneratedClassName( subSystemType );
      if ( subSystemTypes.contains( className ) )
      {
        throw new ProcessorException( MemberChecks.mustNot( Constants.GALDR_STAGE_CLASSNAME,
                                                            "have a duplicate type in the value parameter. " +
                                                            "The type " + subSystem + " appears multiple times" ),
                                      method );
      }
      subSystemTypes.add( className );
    }
    if ( subSystemTypes.isEmpty() )
    {
      throw new ProcessorException( MemberChecks.must( Constants.GALDR_STAGE_CLASSNAME,
                                                       "have at least one SubSystem defined by the value parameter" ),
                                    method );
    }
    descriptor.addStage( new StageDescriptor( name, method, subSystemTypes ) );
  }
}
