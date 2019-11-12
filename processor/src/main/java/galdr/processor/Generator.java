package galdr.processor;

import com.google.auto.common.GeneratedAnnotationSpecs;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

final class Generator
{
  private static final ClassName GALDR_CLASSNAME = ClassName.get( "galdr", "Galdr" );
  private static final ClassName WORLD_CLASSNAME = ClassName.get( "galdr", "World" );
  private static final ClassName NONNULL_CLASSNAME = ClassName.get( "javax.annotation", "Nonnull" );
  private static final ClassName NULLABLE_CLASSNAME = ClassName.get( "javax.annotation", "Nullable" );
  private static final String FRAMEWORK_INTERNAL_PREFIX = "$galdr$_";
  private static final String OUTER_FIELD = FRAMEWORK_INTERNAL_PREFIX + "outer";
  private static final String NAME_ACCESSOR_METHOD = FRAMEWORK_INTERNAL_PREFIX + "getName";
  private static final String WORLD_ACCESSOR_METHOD = FRAMEWORK_INTERNAL_PREFIX + "getWorld";

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

    builder.addMethod( MethodSpec.methodBuilder( WORLD_ACCESSOR_METHOD )
                         .addAnnotation( NONNULL_CLASSNAME )
                         .addModifiers( Modifier.PRIVATE )
                         .returns( WORLD_CLASSNAME )
                         .addStatement( "return $T.current()", WORLD_CLASSNAME )
                         .build() );

    builder.addType( buildEnhancedSubSystem( descriptor ) );

    return builder.build();
  }

  @Nonnull
  private static TypeSpec buildEnhancedSubSystem( @Nonnull final SubSystemDescriptor descriptor )
  {
    final TypeSpec.Builder builder = TypeSpec.classBuilder( "EnhancedSubSystem" );
    builder.addModifiers( Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL );
    builder.superclass( TypeName.get( descriptor.getElement().asType() ) );

    builder.addField( FieldSpec.builder( descriptor.getEnhancedClassName(), OUTER_FIELD, Modifier.FINAL )
                        .addAnnotation( NONNULL_CLASSNAME )
                        .build() );

    builder.addMethod( MethodSpec.constructorBuilder()
                         .addModifiers( Modifier.PRIVATE )
                         .addParameter( ParameterSpec.builder( descriptor.getEnhancedClassName(),
                                                               "outer",
                                                               Modifier.FINAL )
                                          .addAnnotation( NONNULL_CLASSNAME )
                                          .build() )
                         .addStatement( "$N = $T.requireNonNull( outer )", OUTER_FIELD, Objects.class )
                         .build() );

    for ( final ExecutableElement nameRef : descriptor.getNameRefs() )
    {
      final MethodSpec.Builder method =
        MethodSpec
          .methodBuilder( nameRef.getSimpleName().toString() )
          //TODO: .addAnnotation( Override.class )
          .addAnnotation( NONNULL_CLASSNAME )
          .returns( String.class )
          .addStatement( "return $N()", NAME_ACCESSOR_METHOD );
      ProcessorUtil.copyAccessModifiers( nameRef, method );
      builder.addMethod( method.build() );
    }

    for ( final ExecutableElement worldRef : descriptor.getWorldRefs() )
    {
      final MethodSpec.Builder method =
        MethodSpec
          .methodBuilder( worldRef.getSimpleName().toString() )
          //TODO: .addAnnotation( Override.class )
          .addAnnotation( NONNULL_CLASSNAME )
          .returns( WORLD_CLASSNAME )
          .addStatement( "return $N.$N()", OUTER_FIELD, WORLD_ACCESSOR_METHOD );
      ProcessorUtil.copyAccessModifiers( worldRef, method );
      builder.addMethod( method.build() );
    }

    builder.addMethod( MethodSpec.methodBuilder( NAME_ACCESSOR_METHOD )
                         .addAnnotation( NONNULL_CLASSNAME )
                         .addModifiers( Modifier.PRIVATE )
                         .returns( String.class )
                         .addStatement( "return $S", descriptor.getName() )
                         .build() );

    final CodeBlock.Builder toStringBlock = CodeBlock.builder();
    toStringBlock.beginControlFlow( "if ( $T.areDebugToStringMethodsEnabled() )", GALDR_CLASSNAME );
    toStringBlock.addStatement( "return $S + $N() + $S", "SubSystem[", NAME_ACCESSOR_METHOD, "]" );
    toStringBlock.nextControlFlow( "else" );
    toStringBlock.addStatement( "return super.toString()" );
    toStringBlock.endControlFlow();
    builder.addMethod( MethodSpec.methodBuilder( "toString" )
                         .addAnnotation( NONNULL_CLASSNAME )
                         .addAnnotation( Override.class )
                         .addModifiers( Modifier.PUBLIC )
                         .returns( String.class )
                         .addCode( toStringBlock.build() )
                         .build() );

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
