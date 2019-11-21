package galdr.processor;

import com.squareup.javapoet.ClassName;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;

/**
 * Annotation processor that generates application implementations.
 */
@SupportedAnnotationTypes( Constants.GALDR_APPLICATION_CLASSNAME )
@SupportedSourceVersion( SourceVersion.RELEASE_8 )
public final class ApplicationProcessor
  extends AbstractGaldrProcessor
{
  @Override
  @Nonnull
  String getRootAnnotationClassname()
  {
    return Constants.GALDR_APPLICATION_CLASSNAME;
  }

  @Override
  final void process( @Nonnull final TypeElement element )
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
    final AnnotationMirror annotation =
      AnnotationsUtil.getAnnotationByType( element, Constants.GALDR_APPLICATION_CLASSNAME );
    final ApplicationDescriptor descriptor = new ApplicationDescriptor( element );

    final List<ExecutableElement> constructors = ProcessorUtil.getConstructors( element );
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
      ProcessorUtil.getMethods( element, processingEnv.getElementUtils(), processingEnv.getTypeUtils() );
    for ( final ExecutableElement method : methods )
    {
      final AnnotationMirror stage = AnnotationsUtil.findAnnotationByType( method, Constants.GALDR_STAGE_CLASSNAME );

      if ( null != stage )
      {
        addStage( descriptor, method, stage );
      }
    }

    GeneratorUtil.emitJavaType( descriptor.getPackageName(),
                                Generator.buildApplication( processingEnv, descriptor ),
                                processingEnv.getFiler() );
  }

  private void addStage( @Nonnull final ApplicationDescriptor descriptor,
                         @Nonnull final ExecutableElement method,
                         @Nonnull final AnnotationMirror stage )
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
    final String name = method.getSimpleName().toString();
    final List<ClassName> subSystemTypes = new ArrayList<>();
    descriptor.addStage( new StageDescriptor( name, method, subSystemTypes ) );
  }
}
