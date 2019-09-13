package galdr;

import java.util.Objects;
import javax.annotation.Nonnull;
import static org.realityforge.braincheck.Guards.*;

/**
 * A component type descriptor.
 */
final class ComponentType
{
  /**
   * The java type of the component.
   */
  @Nonnull
  private final Class<?> _type;
  /**
   * Unique index of type within a {@link World}.
   * Used to enable fast lookup and access of component data.
   */
  private final int _index;

  ComponentType( @Nonnull final Class<?> type, final int index )
  {
    _type = Objects.requireNonNull( type );
    _index = index;
  }

  /**
   * Return the unique index of type within a {@link World}.
   *
   * @return the unique index of type within a {@link World}.
   */
  int getIndex()
  {
    return _index;
  }

  /**
   * Return the java type of the component.
   *
   * @return the java type of the component.
   */
  @Nonnull
  Class<?> getType()
  {
    return _type;
  }

  /**
   * Return the human readable name of the ComponentType.
   * This method should NOT be invoked unless {@link Galdr#areNamesEnabled()} returns <code>true</code>.
   *
   * @return the human readable name of the ComponentType.
   */
  @Nonnull
  String getName()
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      apiInvariant( Galdr::areNamesEnabled,
                    () -> "Galdr-0053: ComponentType.getName() invoked when Galdr.areNamesEnabled() returns false" );
    }
    return _type.getSimpleName();
  }

  @Override
  public String toString()
  {
    if ( Galdr.areDebugToStringMethodsEnabled() )
    {
      return "ComponentType[" + getName() + "=" + _index + "]";
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
