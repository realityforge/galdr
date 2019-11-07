package galdr.processor;

import com.google.auto.common.GeneratedAnnotationSpecs;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;
import javax.annotation.Nonnull;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

final class Generator
{
  private static final ClassName NONNULL_CLASSNAME = ClassName.get( "javax.annotation", "Nonnull" );
  private static final ClassName NULLABLE_CLASSNAME = ClassName.get( "javax.annotation", "Nullable" );

  private Generator()
  {
  }

  @Nonnull
  static TypeSpec buildSubSystem( @Nonnull final ProcessingEnvironment processingEnv,
                                  @Nonnull final SubSystemDescriptor descriptor )
  {
    final TypeSpec.Builder builder = TypeSpec.classBuilder( descriptor.getEnhancedClassName() );
    addGeneratedAnnotation( processingEnv, builder, SubSystemProcessor.class );
    builder.addModifiers( Modifier.PUBLIC, Modifier.FINAL );

    return builder.build();
  }

  private static void addGeneratedAnnotation( @Nonnull final ProcessingEnvironment processingEnv,
                                              @Nonnull final TypeSpec.Builder builder,
                                              @Nonnull final Class<?> annotationProcessor )
  {
    GeneratedAnnotationSpecs
      .generatedAnnotationSpec( processingEnv.getElementUtils(), processingEnv.getSourceVersion(), annotationProcessor )
      .ifPresent( builder::addAnnotation );
  }

  @Nonnull
  static ClassName toGeneratedClassName( @Nonnull final TypeElement element )
  {
    return GeneratorUtil.getGeneratedClassName( element, "Galdr_", "" );
  }
}
