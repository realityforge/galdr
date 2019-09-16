package galdr;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

/**
 * Base processor class.
 */
public abstract class Processor
{
  /**
   * A human consumable name for node. It should be non-null if {@link Galdr#areNamesEnabled()} returns
   * true and <tt>null</tt> otherwise.
   */
  @Nullable
  private final String _name;
  @Nullable
  private World _world;

  Processor( @Nullable final String name )
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> Galdr.areNamesEnabled() || null == name,
                    () -> "Galdr-0052: Processor passed a name '" + name + "' but Galdr.areNamesEnabled() is false" );
    }
    _name = Galdr.areNamesEnabled() ? Objects.requireNonNull( name ) : null;
  }

  protected abstract void process( int delta );

  /**
   * Initialize World.
   *
   * In the future when an annotation processor processes these classes, this method will be removed and
   * {@link #getWorld()} will be implemented via code generation.
   *
   * @param world the world.
   */
  final void setWorld( @Nonnull final World world )
  {
    assert null == _world;
    _world = Objects.requireNonNull( world );
  }

  @Nonnull
  protected final World getWorld()
  {
    assert null != _world;
    return _world;
  }

  /**
   * Return the human readable name of the Processor.
   * This method should NOT be invoked unless {@link Galdr#areNamesEnabled()} returns <code>true</code>.
   *
   * @return the human readable name of the Processor.
   */
  @Nonnull
  protected final String getName()
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      apiInvariant( Galdr::areNamesEnabled,
                    () -> "Galdr-0004: Processor.getName() invoked when Galdr.areNamesEnabled() returns false" );
    }
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
