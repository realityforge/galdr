package galdr.processor;

import java.util.Objects;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;

@SuppressWarnings( { "SameParameterValue", "WeakerAccess", "unused" } )
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
                               @Nonnull final String scopeAnnotationName,
                               @Nonnull final String annotationName,
                               @Nonnull final Element element )
    throws GaldrProcessorException
  {
    mustBeOverridable( targetType, scopeAnnotationName, annotationName, element );
    mustNotBeAbstract( annotationName, element );
  }

  /**
   * Verifies that the method is not final, static or abstract.
   * The intent is to verify that it can be overridden in sub-class in the same package.
   */
  static void mustBeOverridable( @Nonnull final TypeElement targetType,
                                 @Nonnull final String scopeAnnotationName,
                                 @Nonnull final String annotationName,
                                 @Nonnull final Element element )
    throws GaldrProcessorException
  {
    mustNotBeFinal( annotationName, element );
    mustBeSubclassCallable( targetType, scopeAnnotationName, annotationName, element );
  }

  /**
   * Verifies that the method is not static, abstract or private.
   * The intent is to verify that it can be instance called by sub-class in the same package as the targetType.
   */
  static void mustBeSubclassCallable( @Nonnull final TypeElement targetType,
                                      @Nonnull final String scopeAnnotationName,
                                      @Nonnull final String annotationName,
                                      @Nonnull final Element element )
    throws GaldrProcessorException
  {
    mustNotBeStatic( annotationName, element );
    mustNotBePrivate( annotationName, element );
    mustNotBePackageAccessInDifferentPackage( targetType, scopeAnnotationName, annotationName, element );
  }

  static void mustBeStatic( @Nonnull final String annotationName, @Nonnull final Element element )
    throws GaldrProcessorException
  {
    if ( !element.getModifiers().contains( Modifier.STATIC ) )
    {
      throw new GaldrProcessorException( must( annotationName, "be static" ), element );
    }
  }

  static void mustNotBeStatic( @Nonnull final String annotationName, @Nonnull final Element element )
    throws GaldrProcessorException
  {
    if ( element.getModifiers().contains( Modifier.STATIC ) )
    {
      throw new GaldrProcessorException( mustNot( annotationName, "be static" ), element );
    }
  }

  static void mustBeAbstract( @Nonnull final String annotationName, @Nonnull final Element element )
    throws GaldrProcessorException
  {
    if ( !element.getModifiers().contains( Modifier.ABSTRACT ) )
    {
      throw new GaldrProcessorException( must( annotationName, "be abstract" ), element );
    }
  }

  static void mustNotBeAbstract( @Nonnull final String annotationName, @Nonnull final Element element )
    throws GaldrProcessorException
  {
    if ( element.getModifiers().contains( Modifier.ABSTRACT ) )
    {
      throw new GaldrProcessorException( mustNot( annotationName, "be abstract" ), element );
    }
  }

  static void mustBeFinal( @Nonnull final String annotationName, @Nonnull final Element element )
    throws GaldrProcessorException
  {
    if ( !element.getModifiers().contains( Modifier.FINAL ) )
    {
      throw new GaldrProcessorException( must( annotationName, "be final" ), element );
    }
  }

  static void mustNotBeFinal( @Nonnull final String annotationName, @Nonnull final Element element )
    throws GaldrProcessorException
  {
    if ( element.getModifiers().contains( Modifier.FINAL ) )
    {
      throw new GaldrProcessorException( mustNot( annotationName, "be final" ), element );
    }
  }

  static void mustBePrivate( @Nonnull final String annotationName, @Nonnull final Element element )
    throws GaldrProcessorException
  {
    if ( !element.getModifiers().contains( Modifier.PRIVATE ) )
    {
      throw new GaldrProcessorException( must( annotationName, "be private" ), element );
    }
  }

  static void mustNotBePrivate( @Nonnull final String annotationName, @Nonnull final Element element )
    throws GaldrProcessorException
  {
    if ( element.getModifiers().contains( Modifier.PRIVATE ) )
    {
      throw new GaldrProcessorException( mustNot( annotationName, "be private" ), element );
    }
  }

  static void mustNotBePackageAccessInDifferentPackage( @Nonnull final TypeElement element,
                                                        @Nonnull final String scopeAnnotationName,
                                                        @Nonnull final String annotationName,
                                                        @Nonnull final Element other )
    throws GaldrProcessorException
  {
    final Set<Modifier> modifiers = other.getModifiers();
    final boolean isPackageAccess =
      !modifiers.contains( Modifier.PRIVATE ) &&
      !modifiers.contains( Modifier.PROTECTED ) &&
      !modifiers.contains( Modifier.PUBLIC );

    if ( isPackageAccess )
    {
      final PackageElement packageElement = GeneratorUtil.getPackageElement( element );
      final PackageElement otherPackageElement =
        GeneratorUtil.getPackageElement( (TypeElement) other.getEnclosingElement() );
      if ( !Objects.equals( packageElement.getQualifiedName(), otherPackageElement.getQualifiedName() ) )
      {
        throw new GaldrProcessorException( mustNot( annotationName,
                                                    "be package access if the " +
                                                    ( other instanceof ExecutableElement ? "method" : "field" ) +
                                                    " is in a different package from the class annotated with" +
                                                    toSimpleName( scopeAnnotationName ) ),
                                           other );
      }
    }
  }

  static void mustNotHaveAnyParameters( @Nonnull final String annotationName, @Nonnull final ExecutableElement method )
    throws GaldrProcessorException
  {
    if ( !method.getParameters().isEmpty() )
    {
      throw new GaldrProcessorException( mustNot( annotationName, "have any parameters" ), method );
    }
  }

  static void mustNotReturnAnyValue( @Nonnull final String annotationName, @Nonnull final ExecutableElement method )
    throws GaldrProcessorException
  {
    if ( TypeKind.VOID != method.getReturnType().getKind() )
    {
      throw new GaldrProcessorException( mustNot( annotationName, "return a value" ), method );
    }
  }

  static void mustReturnAValue( @Nonnull final String annotationName, @Nonnull final ExecutableElement method )
    throws GaldrProcessorException
  {
    if ( TypeKind.VOID == method.getReturnType().getKind() )
    {
      throw new GaldrProcessorException( must( annotationName, "return a value" ), method );
    }
  }

  static void mustNotThrowAnyExceptions( @Nonnull final String annotationName,
                                         @Nonnull final ExecutableElement method )
    throws GaldrProcessorException
  {
    if ( !method.getThrownTypes().isEmpty() )
    {
      throw new GaldrProcessorException( mustNot( annotationName, "throw any exceptions" ), method );
    }
  }

  @Nonnull
  private static String must( @Nonnull final String annotationName, @Nonnull final String message )
  {
    return toSimpleName( annotationName ) + " target must " + message;
  }

  @Nonnull
  private static String mustNot( @Nonnull final String annotationName, @Nonnull final String message )
  {
    return must( annotationName, "not " + message );
  }

  @Nonnull
  private static String toSimpleName( @Nonnull final String annotationName )
  {
    return "@" + annotationName.replaceAll( ".*\\.", "" );
  }
}
