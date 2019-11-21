package galdr.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

final class Generator
{
  private static final ClassName AREA_OF_INTEREST_CLASSNAME = ClassName.get( "galdr", "AreaOfInterest" );
  private static final ClassName COMPONENT_MANAGER_CLASSNAME = ClassName.get( "galdr", "ComponentManager" );
  private static final ClassName GALDR_CLASSNAME = ClassName.get( "galdr", "Galdr" );
  private static final ClassName SUBSCRIPTION_CLASSNAME = ClassName.get( "galdr", "Subscription" );
  private static final ClassName WORLD_CLASSNAME = ClassName.get( "galdr", "World" );
  private static final ClassName WORLDS_CLASSNAME = ClassName.get( "galdr", "Worlds" );
  private static final ClassName POST_CONSTRUCT_FN_CLASSNAME = ClassName.get( "galdr.internal", "PostConstructFn" );
  private static final ClassName ON_ACTIVATE_FN_CLASSNAME = ClassName.get( "galdr.internal", "OnActivateFn" );
  private static final ClassName ON_DEACTIVATE_FN_CLASSNAME = ClassName.get( "galdr.internal", "OnDeactivateFn" );
  private static final ClassName PROCESSOR_FN_CLASSNAME = ClassName.get( "galdr", "SubSystem" );
  private static final ClassName NONNULL_CLASSNAME = ClassName.get( "javax.annotation", "Nonnull" );
  private static final ClassName NULLABLE_CLASSNAME = ClassName.get( "javax.annotation", "Nullable" );
  private static final String FRAMEWORK_INTERNAL_PREFIX = "$galdr$_";
  private static final String FRAMEWORK_COMPONENT_PREFIX = "$galdrc$_";
  private static final String FRAMEWORK_SUBSCRIPTION_PREFIX = "$galdrs$_";
  private static final String FRAMEWORK_AREA_OF_INTEREST_PREFIX = "$galdraoi$_";
  private static final String OUTER_FIELD = FRAMEWORK_INTERNAL_PREFIX + "outer";
  private static final String NAME_ACCESSOR_METHOD = FRAMEWORK_INTERNAL_PREFIX + "getName";
  private static final String WORLD_ACCESSOR_METHOD = FRAMEWORK_INTERNAL_PREFIX + "getWorld";
  private static final String POST_CONSTRUCT_METHOD = FRAMEWORK_INTERNAL_PREFIX + "postConstruct";
  private static final String ON_ACTIVATE_METHOD = FRAMEWORK_INTERNAL_PREFIX + "onActivate";
  private static final String ON_DEACTIVATE_METHOD = FRAMEWORK_INTERNAL_PREFIX + "onDeactivate";
  private static final String PROCESS_METHOD = FRAMEWORK_INTERNAL_PREFIX + "process";

  private Generator()
  {
  }

  @Nonnull
  static TypeSpec buildApplication( @Nonnull final ProcessingEnvironment processingEnv,
                                    @Nonnull final ApplicationDescriptor descriptor )
  {
    final TypeSpec.Builder builder = TypeSpec.classBuilder( descriptor.getEnhancedClassName() );
    GeneratorUtil.addOriginatingTypes( descriptor.getElement(), builder );
    GeneratorUtil.addGeneratedAnnotation( processingEnv, builder, ApplicationProcessor.class.getName() );
    builder.addModifiers( Modifier.FINAL );

    final MethodSpec.Builder constructor = MethodSpec.constructorBuilder();
    final StringBuilder sb = new StringBuilder(  );
    final List<Object> params = new ArrayList<>(  );
    sb.append( "final $T world = $T" );
    params.add( WORLD_CLASSNAME );
    params.add( WORLDS_CLASSNAME );
    sb.append( "\n.world()" );
    sb.append( "\n.build()" );
    constructor.addStatement( sb.toString(), params.toArray() );
    builder.addMethod( constructor.build() );
    return builder.build();
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

    if ( !descriptor.getComponentManagerRefs().isEmpty() || !descriptor.getEntityProcessors().isEmpty() )
    {
      builder.addSuperinterface( POST_CONSTRUCT_FN_CLASSNAME );
      builder.addMethod( MethodSpec.methodBuilder( "postConstruct" )
                           .addAnnotation( Override.class )
                           .addModifiers( Modifier.PUBLIC )
                           .addStatement( "_subsystem.$N()", POST_CONSTRUCT_METHOD )
                           .build() );
    }

    if ( !descriptor.getOnActivates().isEmpty() || !descriptor.getEntityProcessors().isEmpty() )
    {
      builder.addSuperinterface( ON_ACTIVATE_FN_CLASSNAME );
      builder.addMethod( MethodSpec.methodBuilder( "activate" )
                           .addAnnotation( Override.class )
                           .addModifiers( Modifier.PUBLIC )
                           .addStatement( "_subsystem.$N()", ON_ACTIVATE_METHOD )
                           .build() );
    }

    if ( !descriptor.getOnDeactivates().isEmpty() || !descriptor.getEntityProcessors().isEmpty() )
    {
      builder.addSuperinterface( ON_DEACTIVATE_FN_CLASSNAME );
      builder.addMethod( MethodSpec.methodBuilder( "deactivate" )
                           .addAnnotation( Override.class )
                           .addModifiers( Modifier.PUBLIC )
                           .addStatement( "_subsystem.$N()", ON_DEACTIVATE_METHOD )
                           .build() );
    }

    if ( !descriptor.getProcessors().isEmpty() || !descriptor.getEntityProcessors().isEmpty() )
    {
      builder.addSuperinterface( PROCESSOR_FN_CLASSNAME );
      builder.addMethod( MethodSpec.methodBuilder( "process" )
                           .addAnnotation( Override.class )
                           .addModifiers( Modifier.PUBLIC )
                           .addParameter( TypeName.INT, "delta", Modifier.FINAL )
                           .addStatement( "_subsystem.$N( delta )", PROCESS_METHOD )
                           .build() );
    }

    builder.addType( buildEnhancedSubSystem( processingEnv, descriptor ) );

    return builder.build();
  }

  @Nonnull
  private static TypeSpec buildEnhancedSubSystem( @Nonnull final ProcessingEnvironment processingEnv,
                                                  @Nonnull final SubSystemDescriptor descriptor )
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

    for ( final EntityProcessorDescriptor entityProcessorDescriptor : descriptor.getEntityProcessors() )
    {
      final String name = entityProcessorDescriptor.getMethod().getSimpleName().toString();
      builder.addField( FieldSpec.builder( SUBSCRIPTION_CLASSNAME,
                                           FRAMEWORK_SUBSCRIPTION_PREFIX + name,
                                           Modifier.PRIVATE )
                          .addAnnotation( NULLABLE_CLASSNAME )
                          .build() );
      builder.addField( FieldSpec.builder( AREA_OF_INTEREST_CLASSNAME,
                                           FRAMEWORK_AREA_OF_INTEREST_PREFIX + name,
                                           Modifier.PRIVATE )
                          .addAnnotation( NULLABLE_CLASSNAME )
                          .build() );
    }

    emitConstructor( descriptor, builder );

    // Generate Ref methods
    emitComponentManagerAccessors( processingEnv, descriptor, builder );
    emitNameAccessors( processingEnv, descriptor, builder );
    emitWorldAccessors( processingEnv, descriptor, builder );

    // Generate lifecycle methods
    emitPostConstruct( descriptor, builder );
    emitOnActivate( descriptor, builder );
    emitOnDeactivate( descriptor, builder );
    emitProcess( descriptor, builder );

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

  private static void emitComponentManagerAccessors( @Nonnull final ProcessingEnvironment processingEnv,
                                                     @Nonnull final SubSystemDescriptor descriptor,
                                                     @Nonnull final TypeSpec.Builder builder )
  {
    for ( final ComponentManagerRefDescriptor componentManagerRef : descriptor.getComponentManagerRefs() )
    {
      final ParameterizedTypeName type =
        ParameterizedTypeName.get( COMPONENT_MANAGER_CLASSNAME, componentManagerRef.getComponentType() );
      final ExecutableElement methodElement = componentManagerRef.getMethod();
      final String fieldName = FRAMEWORK_COMPONENT_PREFIX + methodElement.getSimpleName().toString();
      builder.addField( FieldSpec.builder( type, fieldName, Modifier.PRIVATE )
                          .addAnnotation( NULLABLE_CLASSNAME )
                          .build() );

      builder.addMethod( GeneratorUtil
                           .refMethod( processingEnv, descriptor.getElement(), methodElement )
                           .addStatement( "assert null != $N", fieldName )
                           .addStatement( "return $N", fieldName )
                           .build() );
    }
  }

  private static void emitNameAccessors( @Nonnull final ProcessingEnvironment processingEnv,
                                         @Nonnull final SubSystemDescriptor descriptor,
                                         @Nonnull final TypeSpec.Builder builder )
  {
    for ( final ExecutableElement nameRef : descriptor.getNameRefs() )
    {
      builder.addMethod( GeneratorUtil
                           .refMethod( processingEnv, descriptor.getElement(), nameRef )
                           .addStatement( "return $N()", NAME_ACCESSOR_METHOD )
                           .build() );
    }
  }

  private static void emitWorldAccessors( @Nonnull final ProcessingEnvironment processingEnv,
                                          @Nonnull final SubSystemDescriptor descriptor,
                                          @Nonnull final TypeSpec.Builder builder )
  {
    for ( final ExecutableElement worldRef : descriptor.getWorldRefs() )
    {
      builder.addMethod( GeneratorUtil
                           .refMethod( processingEnv, descriptor.getElement(), worldRef )
                           .addStatement( "return $N.$N()", OUTER_FIELD, WORLD_ACCESSOR_METHOD )
                           .build() );
    }
  }

  private static void emitPostConstruct( @Nonnull final SubSystemDescriptor descriptor,
                                         @Nonnull final TypeSpec.Builder builder )
  {
    if ( !descriptor.getComponentManagerRefs().isEmpty() || !descriptor.getEntityProcessors().isEmpty() )
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
      for ( final EntityProcessorDescriptor entityProcessorDescriptor : descriptor.getEntityProcessors() )
      {
        final String name = entityProcessorDescriptor.getMethod().getSimpleName().toString();
        final StringBuilder sb = new StringBuilder( "$N = $N.$N().createAreaOfInterest( " );
        final List<Object> params = new ArrayList<>();
        params.add( FRAMEWORK_AREA_OF_INTEREST_PREFIX + name );
        params.add( OUTER_FIELD );
        params.add( WORLD_ACCESSOR_METHOD );

        emitTypeCollection( sb, params, entityProcessorDescriptor.getAll() );
        final List<TypeName> one = entityProcessorDescriptor.getOne();
        final List<TypeName> exclude = entityProcessorDescriptor.getExclude();
        if ( !exclude.isEmpty() )
        {
          sb.append( ", " );
          emitTypeCollection( sb, params, one );
          sb.append( ", " );
          emitTypeCollection( sb, params, exclude );
        }
        else if ( !one.isEmpty() )
        {
          sb.append( ", " );
          emitTypeCollection( sb, params, one );
        }

        sb.append( " )" );
        method.addStatement( sb.toString(), params.toArray() );
      }

      builder.addMethod( method.build() );
    }
  }

  private static void emitTypeCollection( @Nonnull final StringBuilder sb,
                                          @Nonnull final List<Object> params,
                                          @Nonnull final List<TypeName> typeNames )
  {
    if ( typeNames.isEmpty() )
    {
      sb.append( "$T.emptyList()" );
      params.add( Collections.class );
    }
    else if ( 1 == typeNames.size() )
    {
      sb.append( "$T.singleton( $T.class )" );
      params.add( Collections.class );
      params.add( typeNames.get( 0 ) );
    }
    else
    {
      sb.append( "$T.asList( " );
      params.add( Arrays.class );
      sb.append( typeNames.stream().map( t -> "$T.class" ).collect( Collectors.joining( "," ) ) );
      params.addAll( typeNames );
      sb.append( " )" );
    }
  }

  private static void emitOnActivate( @Nonnull final SubSystemDescriptor descriptor,
                                      @Nonnull final TypeSpec.Builder builder )
  {
    if ( !descriptor.getOnActivates().isEmpty() || !descriptor.getEntityProcessors().isEmpty() )
    {
      final MethodSpec.Builder method =
        MethodSpec
          .methodBuilder( ON_ACTIVATE_METHOD )
          .addModifiers( Modifier.PRIVATE );
      for ( final EntityProcessorDescriptor entityProcessorDescriptor : descriptor.getEntityProcessors() )
      {
        final String name = entityProcessorDescriptor.getMethod().getSimpleName().toString();
        method.addStatement( "assert null == $N", FRAMEWORK_SUBSCRIPTION_PREFIX + name );
        method.addStatement( "$N = $N.$N().createSubscription( $T.areNamesEnabled() ? $N() : null, $N )",
                             FRAMEWORK_SUBSCRIPTION_PREFIX + name,
                             OUTER_FIELD,
                             WORLD_ACCESSOR_METHOD,
                             GALDR_CLASSNAME,
                             NAME_ACCESSOR_METHOD,
                             FRAMEWORK_AREA_OF_INTEREST_PREFIX + name );
      }
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
    if ( !descriptor.getOnDeactivates().isEmpty() || !descriptor.getEntityProcessors().isEmpty() )
    {
      final MethodSpec.Builder method =
        MethodSpec
          .methodBuilder( ON_DEACTIVATE_METHOD )
          .addModifiers( Modifier.PRIVATE );
      for ( final ExecutableElement onDeactivate : descriptor.getOnDeactivates() )
      {
        method.addStatement( "$N()", onDeactivate.getSimpleName().toString() );
      }
      for ( final EntityProcessorDescriptor entityProcessorDescriptor : descriptor.getEntityProcessors() )
      {
        final String name =
          FRAMEWORK_SUBSCRIPTION_PREFIX + entityProcessorDescriptor.getMethod().getSimpleName().toString();
        method.addStatement( "assert null != $N", name );
        method.addStatement( "$N.dispose()", name );
        method.addStatement( "$N = null", name );
      }

      builder.addMethod( method.build() );
    }
  }

  private static void emitProcess( @Nonnull final SubSystemDescriptor descriptor,
                                   @Nonnull final TypeSpec.Builder builder )
  {
    if ( !descriptor.getProcessors().isEmpty() || !descriptor.getEntityProcessors().isEmpty() )
    {
      final MethodSpec.Builder method =
        MethodSpec
          .methodBuilder( PROCESS_METHOD )
          .addParameter( TypeName.INT, "delta", Modifier.FINAL )
          .addModifiers( Modifier.PRIVATE );
      for ( final ExecutableElement process : descriptor.getProcessors() )
      {
        if ( process.getParameters().isEmpty() )
        {
          method.addStatement( "$N()", process.getSimpleName().toString() );
        }
        else
        {
          method.addStatement( "$N( delta )", process.getSimpleName().toString() );
        }
      }
      for ( final EntityProcessorDescriptor entityProcessorDescriptor : descriptor.getEntityProcessors() )
      {
        final ExecutableElement sourceMethod = entityProcessorDescriptor.getMethod();
        final String name = sourceMethod.getSimpleName().toString();
        final String subscriptionFieldName = FRAMEWORK_SUBSCRIPTION_PREFIX + name;
        final String entityId = subscriptionFieldName + "_entityId";
        method.addStatement( "assert null != $N", subscriptionFieldName );
        method.addStatement( "$N.beginIteration()", subscriptionFieldName );
        method.addStatement( "int $N", entityId );
        final CodeBlock.Builder block = CodeBlock.builder();
        block.beginControlFlow( "while ( -1 != ( $N = $N.nextEntity() ) )", entityId, subscriptionFieldName );
        if ( 1 == sourceMethod.getParameters().size() )
        {
          block.addStatement( "$N( $N )", name, entityId );
        }
        else
        {
          block.addStatement( "$N( delta, $N )", name, entityId );
        }
        block.endControlFlow();
        method.addCode( block.build() );
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
