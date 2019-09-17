package galdr;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

/**
 * The base class for active elements within the system.
 */
public abstract class Element
{
  /**
   * A human consumable name for node. It should be non-null if {@link Galdr#areNamesEnabled()} returns
   * true and <tt>null</tt> otherwise.
   */
  @Nullable
  private final String _name;

  Element()
  {
    this( null );
  }

  Element( @Nullable final String name )
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> Galdr.areNamesEnabled() || null == name,
                    () -> "Galdr-0052: Element passed a name '" + name + "' but Galdr.areNamesEnabled() is false" );
    }
    _name = Galdr.areNamesEnabled() ? null == name ? getClass().getSimpleName() : name : null;
  }

  /**
   * Return the human readable name of the Element.
   * This method should NOT be invoked unless {@link Galdr#areNamesEnabled()} returns <code>true</code>.
   *
   * @return the human readable name of the Element.
   */
  @Nonnull
  protected final String getName()
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      apiInvariant( Galdr::areNamesEnabled,
                    () -> "Galdr-0004: Element.getName() invoked when Galdr.areNamesEnabled() returns false" );
    }
    assert null != _name;
    return _name;
  }

  @Override
  public String toString()
  {
    if ( Galdr.areDebugToStringMethodsEnabled() )
    {
      return getBaseTypeName() + "[" + getName() + "]";
    }
    else
    {
      return super.toString();
    }
  }

  @Nonnull
  protected abstract String getBaseTypeName();
}
