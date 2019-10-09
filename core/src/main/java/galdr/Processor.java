package galdr;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

/**
 * Base processor class.
 */
public abstract class Processor
{
  /**
   * A human consumable name for Processor. It must be non-null if {@link Galdr#areNamesEnabled()} returns
   * true and <tt>null</tt> otherwise.
   */
  @Nullable
  private final String _name;

  protected Processor()
  {
    this( null );
  }

  protected Processor( @Nullable final String name )
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> Galdr.areNamesEnabled() || null == name,
                    () -> "Galdr-0052: Processor passed a name '" + name + "' but Galdr.areNamesEnabled() is false" );
    }
    _name = Galdr.areNamesEnabled() ? null == name ? getClass().getSimpleName() : name : null;
  }

  protected abstract void process( int delta );

  /**
   * Return the human readable name of the Processor.
   * This method should NOT be invoked unless {@link Galdr#areNamesEnabled()} returns <code>true</code>.
   *
   * @return the human readable name of the Processor.
   */
  @Nonnull
  public final String getName()
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      apiInvariant( Galdr::areNamesEnabled,
                    () -> "Galdr-0004: Processor.getName() invoked when Galdr.areNamesEnabled() returns false" );
    }
    assert null != _name;
    return _name;
  }

  @Override
  public String toString()
  {
    if ( Galdr.areDebugToStringMethodsEnabled() )
    {
      return "Processor[" + getName() + "]";
    }
    else
    {
      return super.toString();
    }
  }
}
