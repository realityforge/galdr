package galdr.processor;

import java.util.Objects;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;

@SuppressWarnings( "SameParameterValue" )
final class MethodChecks
{
  private MethodChecks()
  {
  }

  /**
   * Verifies that the method is not final, static, abstract or private.
   * The intent is to verify that it can be overridden and wrapped in a sub-class in the same package.
   */
  static void mustBeWrappable( @Nonnull final TypeElement targetType,
                               @Nonnull final String annotationName,
                               @Nonnull final Element element )
    throws GaldrProcessorException
  {
    mustBeOverridable( targetType, annotationName, element );
    mustNotBeAbstract( annotationName, element );
  }

  /**
   * Verifies that the method is not final, static or abstract.
   * The intent is to verify that it can be overridden in sub-class in same package.
   */
  static void mustBeOverridable( @Nonnull final TypeElement targetType,
                                 @Nonnull final String annotationName,
                                 @Nonnull final Element element )
    throws GaldrProcessorException
  {
    mustNotBeFinal( annotationName, element );
    mustBeSubclassCallable( targetType, annotationName, element );
  }

  /**
   * Verifies that the method is not static, abstract or private.
   * The intent is to verify that it can be instance called by sub-class in same package.
   */
  static void mustBeSubclassCallable( @Nonnull final TypeElement targetType,
                                      @Nonnull final String annotationName,
                                      @Nonnull final Element element )
    throws GaldrProcessorException
  {
    mustNotBeStatic( annotationName, element );
    mustNotBePrivate( annotationName, element );
    mustNotBePackageAccessInDifferentPackage( targetType, annotationName, element );
  }

  /**
   * Verifies that the method follows conventions of a lifecycle hook.
   * The intent is to verify that it can be instance called by sub-class in same
   * package at a lifecycle stage. It should not raise errors, return values or accept
   * parameters.
   */
  static void mustBeLifecycleHook( @Nonnull final TypeElement targetType,
                                   @Nonnull final String annotationName,
                                   @Nonnull final ExecutableElement method )
    throws GaldrProcessorException
  {
    mustNotBeAbstract( annotationName, method );
    mustBeSubclassCallable( targetType, annotationName, method );
    mustNotHaveAnyParameters( annotationName, method );
    mustNotReturnAnyValue( annotationName, method );
    mustNotThrowAnyExceptions( annotationName, method );
  }

  private static void mustNotBeStatic( @Nonnull final String annotationName, @Nonnull final Element element )
    throws GaldrProcessorException
  {
    if ( element.getModifiers().contains( Modifier.STATIC ) )
    {
      throw new GaldrProcessorException( "@" + ProcessorUtil.toSimpleName( annotationName ) +
                                         " target must not be static", element );
    }
  }

  static void mustNotBeAbstract( @Nonnull final String annotationName, @Nonnull final Element element )
    throws GaldrProcessorException
  {
    if ( element.getModifiers().contains( Modifier.ABSTRACT ) )
    {
      throw new GaldrProcessorException( "@" + ProcessorUtil.toSimpleName( annotationName ) +
                                         " target must not be abstract", element );
    }
  }

  static void mustBeAbstract( @Nonnull final String annotationName, @Nonnull final Element element )
    throws GaldrProcessorException
  {
    if ( !element.getModifiers().contains( Modifier.ABSTRACT ) )
    {
      throw new GaldrProcessorException( "@" +
                                         ProcessorUtil.toSimpleName( annotationName ) +
                                         " target must be abstract",
                                         element );
    }
  }

  private static void mustNotBePrivate( @Nonnull final String annotationName,
                                        @Nonnull final Element element )
    throws GaldrProcessorException
  {
    if ( element.getModifiers().contains( Modifier.PRIVATE ) )
    {
      throw new GaldrProcessorException( "@" + ProcessorUtil.toSimpleName( annotationName ) +
                                         " target must not be private", element );
    }
  }

  static void mustNotBePackageAccessInDifferentPackage( @Nonnull final TypeElement component,
                                                        @Nonnull final String annotationName,
                                                        @Nonnull final Element element )
    throws GaldrProcessorException
  {
    final Set<Modifier> modifiers = element.getModifiers();
    final boolean isPackageAccess =
      !modifiers.contains( Modifier.PRIVATE ) &&
      !modifiers.contains( Modifier.PROTECTED ) &&
      !modifiers.contains( Modifier.PUBLIC );

    if ( isPackageAccess )
    {
      final PackageElement packageElement = ProcessorUtil.getPackageElement( component );
      final PackageElement methodPackageElement =
        ProcessorUtil.getPackageElement( (TypeElement) element.getEnclosingElement() );
      final Name componentPackageName = packageElement.getQualifiedName();
      final Name methodPackageName = methodPackageElement.getQualifiedName();
      if ( !Objects.equals( componentPackageName, methodPackageName ) )
      {
        throw new GaldrProcessorException( "@" + ProcessorUtil.toSimpleName( annotationName ) + " target must " +
                                           "not be package access if the method is in a different package from " +
                                           "the @GaldrApplication", element );
      }
    }
  }

  private static void mustNotBeFinal( @Nonnull final String annotationName, @Nonnull final Element Element )
    throws GaldrProcessorException
  {
    if ( Element.getModifiers().contains( Modifier.FINAL ) )
    {
      throw new GaldrProcessorException( "@" + ProcessorUtil.toSimpleName( annotationName ) +
                                         " target must not be final", Element );
    }
  }

  static void mustBeFinal( @Nonnull final String annotationName, @Nonnull final Element element )
    throws GaldrProcessorException
  {
    if ( !element.getModifiers().contains( Modifier.FINAL ) )
    {
      throw new GaldrProcessorException( "@" + ProcessorUtil.toSimpleName( annotationName ) + " target must be final",
                                         element );
    }
  }

  static void mustNotHaveAnyParameters( @Nonnull final String annotationName, @Nonnull final ExecutableElement method )
    throws GaldrProcessorException
  {
    if ( !method.getParameters().isEmpty() )
    {
      throw new GaldrProcessorException( "@" + ProcessorUtil.toSimpleName( annotationName ) +
                                         " target must not have any parameters", method );
    }
  }

  static void mustNotReturnAnyValue( @Nonnull final String annotationName, @Nonnull final ExecutableElement method )
    throws GaldrProcessorException
  {
    if ( TypeKind.VOID != method.getReturnType().getKind() )
    {
      throw new GaldrProcessorException( "@" + ProcessorUtil.toSimpleName( annotationName ) +
                                         " target must not return a value", method );
    }
  }

  static void mustReturnAValue( @Nonnull final String annotationName,
                                @Nonnull final ExecutableElement method )
    throws GaldrProcessorException
  {
    if ( TypeKind.VOID == method.getReturnType().getKind() )
    {
      throw new GaldrProcessorException( "@" + ProcessorUtil.toSimpleName( annotationName ) +
                                         " target must return a value", method );
    }
  }

  static void mustNotThrowAnyExceptions( @Nonnull final String annotationName,
                                         @Nonnull final ExecutableElement method )
    throws GaldrProcessorException
  {
    if ( !method.getThrownTypes().isEmpty() )
    {
      throw new GaldrProcessorException( "@" + ProcessorUtil.toSimpleName( annotationName ) +
                                         " target must not throw any exceptions", method );
    }
  }
}