package galdr.processor;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.lang.model.element.TypeElement;

final class SubSystemDescriptor
{
  @Nonnull
  private final TypeElement _element;
  @Nonnull
  private final String _name;
  private final int _priority;

  SubSystemDescriptor( @Nonnull final TypeElement element, @Nonnull final String name, final int priority )
  {
    _element = Objects.requireNonNull( element );
    _name = Objects.requireNonNull( name );
    _priority = priority;
  }

  @Nonnull
  TypeElement getElement()
  {
    return _element;
  }

  @Nonnull
  String getName()
  {
    return _name;
  }

  int getPriority()
  {
    return _priority;
  }
}
