package galdr.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
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
  private static final ClassName COMPONENT_MANAGER_CLASSNAME = ClassName.get( "galdr", "ComponentManager" );
  private static final ClassName GALDR_CLASSNAME = ClassName.get( "galdr", "Galdr" );
  private static final ClassName WORLD_CLASSNAME = ClassName.get( "galdr", "World" );
  private static final ClassName POST_CONSTRUCT_FN_CLASSNAME = ClassName.get( "galdr.internal", "PostConstructFn" );
  private static final ClassName ON_ACTIVATE_FN_CLASSNAME = ClassName.get( "galdr.internal", "OnActivateFn" );
  private static final ClassName ON_DEACTIVATE_FN_CLASSNAME = ClassName.get( "galdr.internal", "OnDeactivateFn" );
  private static final ClassName NONNULL_CLASSNAME = ClassName.get( "javax.annotation", "Nonnull" );
  private static final ClassName NULLABLE_CLASSNAME = ClassName.get( "javax.annotation", "Nullable" );
  private static final String FRAMEWORK_INTERNAL_PREFIX = "$galdr$_";
  private static final String FRAMEWORK_COMPONENT_PREFIX = "$galdrc$_";
  private static final String OUTER_FIELD = FRAMEWORK_INTERNAL_PREFIX + "outer";
  private static final String NAME_ACCESSOR_METHOD = FRAMEWORK_INTERNAL_PREFIX + "getName";
  private static final String WORLD_ACCESSOR_METHOD = FRAMEWORK_INTERNAL_PREFIX + "getWorld";
  private static final String POST_CONSTRUCT_METHOD = FRAMEWORK_INTERNAL_PREFIX + "postConstruct";
  private static final String ON_ACTIVATE_METHOD = FRAMEWORK_INTERNAL_PREFIX + "onActivate";
  private static final String ON_DEACTIVATE_METHOD = FRAMEWORK_INTERNAL_PREFIX + "onDeactivate";

  private Generator()
  {
  }

  @Nonnull
  static TypeSpec buildSubSystem( @Nonnull final ProcessingEnvironment processingEnv,
                                  @Nonnull final SubSystemDescriptor descriptor )
  {
    final TypeSpec.Builder builder = TypeSpec.classBuilder( descriptor.getEnhancedClassName() );
    GeneratorUtil.addOriginatingTypes( descriptor.getElement(), builder );
    GeneratorUtil.addGeneratedAnnotation( processingEnv, builder, SubSystemProcessor.class.getName() );
    builder.addModifiers( Modifier.PUBLIC, Modifier.FINAL );

    final ClassName enhancedSubSystem = ClassName.bestGuess( "EnhancedSubSystem" );
    builder.addField( FieldSpec
                        .builder( enhancedSubSystem, "_subsystem", Modifier.PRIVATE, Modifier.FINAL )
                        .addAnnotation( NONNULL_CLASSNAME )
                        .initializer( "new $T( this )", enhancedSubSystem )
                        .build() );

    builder.addMethod( MethodSpec.methodBuilder( WORLD_ACCESSOR_METHOD )
                         .addAnnotation( NONNULL_CLASSNAME )
                         .addModifiers( Modifier.PRIVATE )
                         .returns( WORLD_CLASSNAME )
                         .addStatement( "return $T.current()", WORLD_CLASSNAME )
                         .build() );

    if ( !descriptor.getComponentManagerRefs().isEmpty() )
    {
      builder.addSuperinterface( POST_CONSTRUCT_FN_CLASSNAME );
      builder.addMethod( MethodSpec.methodBuilder( "postConstruct" )
                           .addAnnotation( Override.class )
                           .addModifiers( Modifier.PUBLIC )
                           .addStatement( "_subsystem.$N()", POST_CONSTRUCT_METHOD )
                           .build() );
    }

    if ( !descriptor.getOnActivates().isEmpty() )
    {
      builder.addSuperinterface( ON_ACTIVATE_FN_CLASSNAME );
      builder.addMethod( MethodSpec.methodBuilder( "activate" )
                           .addAnnotation( Override.class )
                           .addModifiers( Modifier.PUBLIC )
                           .addStatement( "_subsystem.$N()", ON_ACTIVATE_METHOD )
                           .build() );
    }

    if ( !descriptor.getOnDeactivates().isEmpty() )
    {
      builder.addSuperinterface( ON_DEACTIVATE_FN_CLASSNAME );
      builder.addMethod( MethodSpec.methodBuilder( "deactivate" )
                           .addAnnotation( Override.class )
                           .addModifiers( Modifier.PUBLIC )
                           .addStatement( "_subsystem.$N()", ON_DEACTIVATE_METHOD )
                           .build() );
    }

    builder.addType( buildEnhancedSubSystem( descriptor ) );

    return builder.build();
  }

  @Nonnull
  private static TypeSpec buildEnhancedSubSystem( @Nonnull final SubSystemDescriptor descriptor )
  {
    final TypeSpec.Builder builder = TypeSpec.classBuilder( "EnhancedSubSystem" );
    builder.addModifiers( Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL );
    builder.superclass( TypeName.get( descriptor.getElement().asType() ) );

    builder.addField( FieldSpec.builder( descriptor.getEnhancedClassName(),
                                         OUTER_FIELD,
                                         Modifier.PRIVATE,
                                         Modifier.FINAL )
                        .addAnnotation( NONNULL_CLASSNAME )
                        .build() );

    emitConstructor( descriptor, builder );

    // Generate Ref methods
    emitComponentManagerAccessors( descriptor, builder );
    emitNameAccessors( descriptor, builder );
    emitWorldAccessors( descriptor, builder );

    // Generate lifecycle methods
    emitPostConstruct( descriptor, builder );
    emitOnActivate( descriptor, builder );
    emitOnDeactivate( descriptor, builder );

    // Generate support code
    emitNativeNameMethod( descriptor, builder );
    emitToString( builder );

    return builder.build();
  }

  private static void emitConstructor( @Nonnull final SubSystemDescriptor descriptor,
                                       @Nonnull final TypeSpec.Builder builder )
  {
    builder.addMethod( MethodSpec.constructorBuilder()
                         .addModifiers( Modifier.PRIVATE )
                         .addParameter( ParameterSpec.builder( descriptor.getEnhancedClassName(),
                                                               "outer",
                                                               Modifier.FINAL )
                                          .addAnnotation( NONNULL_CLASSNAME )
                                          .build() )
                         .addStatement( "$N = $T.requireNonNull( outer )", OUTER_FIELD, Objects.class )
                         .build() );
  }

  private static void emitComponentManagerAccessors( @Nonnull final SubSystemDescriptor descriptor,
                                                     @Nonnull final TypeSpec.Builder builder )
  {
    for ( final ComponentManagerRefDescriptor componentManagerRef : descriptor.getComponentManagerRefs() )
    {
      final ParameterizedTypeName type =
        ParameterizedTypeName.get( COMPONENT_MANAGER_CLASSNAME, componentManagerRef.getComponentType() );
      final ExecutableElement methodElement = componentManagerRef.getMethod();
      final String methodName = methodElement.getSimpleName().toString();
      final String fieldName = FRAMEWORK_COMPONENT_PREFIX + methodName;
      builder.addField( FieldSpec.builder( type, fieldName, Modifier.PRIVATE )
                          .addAnnotation( NULLABLE_CLASSNAME )
                          .build() );

      final MethodSpec.Builder method =
        MethodSpec
          .methodBuilder( methodName )
          .addAnnotation( Override.class )
          .addAnnotation( NONNULL_CLASSNAME )
          .returns( type )
          .addStatement( "assert null != $N", fieldName )
          .addStatement( "return $N", fieldName );
      GeneratorUtil.copyAccessModifiers( methodElement, method );
      builder.addMethod( method.build() );
    }
  }

  private static void emitNameAccessors( @Nonnull final SubSystemDescriptor descriptor,
                                         @Nonnull final TypeSpec.Builder builder )
  {
    for ( final ExecutableElement nameRef : descriptor.getNameRefs() )
    {
      final MethodSpec.Builder method =
        MethodSpec
          .methodBuilder( nameRef.getSimpleName().toString() )
          .addAnnotation( Override.class )
          .addAnnotation( NONNULL_CLASSNAME )
          .returns( String.class )
          .addStatement( "return $N()", NAME_ACCESSOR_METHOD );
      GeneratorUtil.copyAccessModifiers( nameRef, method );
      builder.addMethod( method.build() );
    }
  }

  private static void emitWorldAccessors( @Nonnull final SubSystemDescriptor descriptor,
                                          final TypeSpec.Builder builder )
  {
    for ( final ExecutableElement worldRef : descriptor.getWorldRefs() )
    {
      final MethodSpec.Builder method =
        MethodSpec
          .methodBuilder( worldRef.getSimpleName().toString() )
          .addAnnotation( Override.class )
          .addAnnotation( NONNULL_CLASSNAME )
          .returns( WORLD_CLASSNAME )
          .addStatement( "return $N.$N()", OUTER_FIELD, WORLD_ACCESSOR_METHOD );
      GeneratorUtil.copyAccessModifiers( worldRef, method );
      builder.addMethod( method.build() );
    }
  }

  private static void emitPostConstruct( @Nonnull final SubSystemDescriptor descriptor,
                                         @Nonnull final TypeSpec.Builder builder )
  {
    if ( !descriptor.getComponentManagerRefs().isEmpty() )
    {
      final MethodSpec.Builder method =
        MethodSpec
          .methodBuilder( POST_CONSTRUCT_METHOD )
          .addModifiers( Modifier.PRIVATE );
      for ( final ComponentManagerRefDescriptor componentManagerRef : descriptor.getComponentManagerRefs() )
      {
        final ExecutableElement methodElement = componentManagerRef.getMethod();
        final String methodName = methodElement.getSimpleName().toString();
        final String fieldName = FRAMEWORK_COMPONENT_PREFIX + methodName;

        method.addStatement( "$N = $N.$N().getComponentByType( $T.class )",
                             fieldName,
                             OUTER_FIELD,
                             WORLD_ACCESSOR_METHOD,
                             componentManagerRef.getComponentType() );
      }

      builder.addMethod( method.build() );
    }
  }

  private static void emitOnActivate( @Nonnull final SubSystemDescriptor descriptor,
                                         @Nonnull final TypeSpec.Builder builder )
  {
    if ( !descriptor.getOnActivates().isEmpty() )
    {
      final MethodSpec.Builder method =
        MethodSpec
          .methodBuilder( ON_ACTIVATE_METHOD )
          .addModifiers( Modifier.PRIVATE );
      for ( final ExecutableElement onActivate : descriptor.getOnActivates() )
      {
        method.addStatement( "$N()", onActivate.getSimpleName().toString() );
      }

      builder.addMethod( method.build() );
    }
  }

  private static void emitOnDeactivate( @Nonnull final SubSystemDescriptor descriptor,
                                         @Nonnull final TypeSpec.Builder builder )
  {
    if ( !descriptor.getOnDeactivates().isEmpty() )
    {
      final MethodSpec.Builder method =
        MethodSpec
          .methodBuilder( ON_DEACTIVATE_METHOD )
          .addModifiers( Modifier.PRIVATE );
      for ( final ExecutableElement onDeactivate : descriptor.getOnDeactivates() )
      {
        method.addStatement( "$N()", onDeactivate.getSimpleName().toString() );
      }

      builder.addMethod( method.build() );
    }
  }

  private static void emitNativeNameMethod( @Nonnull final SubSystemDescriptor descriptor,
                                            @Nonnull final TypeSpec.Builder builder )
  {
    builder.addMethod( MethodSpec.methodBuilder( NAME_ACCESSOR_METHOD )
                         .addAnnotation( NONNULL_CLASSNAME )
                         .addModifiers( Modifier.PRIVATE )
                         .returns( String.class )
                         .addStatement( "return $S", descriptor.getName() )
                         .build() );
  }

  private static void emitToString( @Nonnull final TypeSpec.Builder builder )
  {
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
  }

  @Nonnull
  static ClassName toGeneratedClassName( @Nonnull final TypeElement element )
  {
    return GeneratorUtil.getGeneratedClassName( element, "Galdr_", "" );
  }
}
