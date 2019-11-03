package galdr;

import grim.annotations.OmitSymbol;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

/**
 * Container for processor instances.
 */
final class ProcessorEntry
{
  /**
   * A human consumable name for the processor. It must be non-null if {@link Galdr#areNamesEnabled()} returns
   * true and <tt>null</tt> otherwise.
   */
  @OmitSymbol( unless = "galdr.enable_names" )
  @Nullable
  private final String _name;
  /**
   * The processor.
   */
  @Nonnull
  private final ProcessorFn _processor;

  ProcessorEntry( @Nullable final String name, @Nonnull final ProcessorFn processor )
  {
    _name = Galdr.areNamesEnabled() ? null == name ? processor.getClass().getSimpleName() : name : null;
    _processor = Objects.requireNonNull( processor );
  }

  /**
   * Return the human readable name of the entry.
   * This method should NOT be invoked unless {@link Galdr#areNamesEnabled()} returns <code>true</code>.
   *
   * @return the human readable name of the World.
   */
  @OmitSymbol( unless = "galdr.enable_names" )
  @Nonnull
  final String getName()
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      apiInvariant( Galdr::areNamesEnabled,
                    () -> "Galdr-0004: ProcessorEntry.getName() invoked when Galdr.areNamesEnabled() returns false" );
    }
    assert null != _name;
    return _name;
  }

  @Nonnull
  ProcessorFn getProcessor()
  {
    return _processor;
  }

  @OmitSymbol( unless = "galdr.debug_to_string" )
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
