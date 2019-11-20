package galdr.processor;

import com.squareup.javapoet.ClassName;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.lang.model.element.TypeElement;

abstract class AbstractDescriptor
{
  @Nonnull
  private final TypeElement _element;

  AbstractDescriptor( @Nonnull final TypeElement element )
  {
    _element = Objects.requireNonNull( element );
  }

  @Nonnull
  final TypeElement getElement()
  {
    return _element;
  }

  @Nonnull
  final String getPackageName()
  {
    return GeneratorUtil.getQualifiedPackageName( getElement() );
  }

  @Nonnull
  final ClassName getEnhancedClassName()
  {
    return Generator.toGeneratedClassName( getElement() );
  }
}
