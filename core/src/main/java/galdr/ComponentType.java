package galdr;

import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * A component type descriptor.
 */
public final class ComponentType
{
  @Nonnull
  private final Class<? extends Component> _type;
  /**
   * Unique index of type within a {@link World}.
   * Used to enable fast lookup and access of component data.
   */
  private final int _index;

  ComponentType( @Nonnull final Class<? extends Component> type, final int index )
  {
    _type = Objects.requireNonNull( type );
    _index = index;
  }

  /**
   * Return the unique index of type within a {@link World}.
   *
   * @return the unique index of type within a {@link World}.
   */
  public int getIndex()
  {
    return _index;
  }

  /**
   * Return the type of the component.
   *
   * @return the type of the component.
   */
  public Class<? extends Component> getType()
  {
    return _type;
  }

  @Override
  public String toString()
  {
    if ( Galdr.areDebugToStringMethodsEnabled() )
    {
      return "ComponentType[" + _index + "=" + _type.getSimpleName() + "]";
    }
    else
    {
      return super.toString();
    }
  }

  @Override
  public boolean equals( final Object o )
  {
    return o instanceof ComponentType && _index == ( (ComponentType) o )._index;
  }

  @Override
  public int hashCode()
  {
    return _index;
  }
}
