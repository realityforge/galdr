package galdr;

import grim.annotations.OmitSymbol;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

/**
 * Container for SubSystem instances.
 */
final class SubSystemEntry
{
  /**
   * A human consumable name for the SubSystem. It must be non-null if {@link Galdr#areNamesEnabled()} returns
   * true and <tt>null</tt> otherwise.
   */
  @OmitSymbol( unless = "galdr.enable_names" )
  @Nullable
  private final String _name;
  /**
   * The SubSystem.
   */
  @Nonnull
  private final SubSystem _subSystem;

  SubSystemEntry( @Nullable final String name, @Nonnull final SubSystem subSystem )
  {
    _name = Galdr.areNamesEnabled() ? null == name ? subSystem.getClass().getSimpleName() : name : null;
    _subSystem = Objects.requireNonNull( subSystem );
  }

  /**
   * Return the human readable name of the SubSystem.
   * This method should NOT be invoked unless {@link Galdr#areNamesEnabled()} returns <code>true</code>.
   *
   * @return the human readable name of the SubSystem.
   */
  @OmitSymbol( unless = "galdr.enable_names" )
  @Nonnull
  final String getName()
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      apiInvariant( Galdr::areNamesEnabled,
                    () -> "Galdr-0004: SubSystemEntry.getName() invoked when Galdr.areNamesEnabled() returns false" );
    }
    assert null != _name;
    return _name;
  }

  @Nonnull
  SubSystem getSubSystem()
  {
    return _subSystem;
  }

  @OmitSymbol( unless = "galdr.debug_to_string" )
  @Override
  public String toString()
  {
    if ( Galdr.areDebugToStringMethodsEnabled() )
    {
      return "SubSystem[" + getName() + "]";
    }
    else
    {
      return super.toString();
    }
  }
}
