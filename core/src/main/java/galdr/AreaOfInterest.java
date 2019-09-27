package galdr;

import java.util.BitSet;
import javax.annotation.Nonnull;

/**
 * Represents a group of components that are of interest.
 * Typically this is used by a {@link galdr.Processor} to describe the {@link galdr.Entity} instances
 * that will be selected by the processor for processing.
 */
public final class AreaOfInterest
{
  @Nonnull
  private final BitSet _and = new BitSet();
  @Nonnull
  private final BitSet _or = new BitSet();
  @Nonnull
  private final BitSet _not = new BitSet();

  /**
   * Return true if the specified componentIds matches the area of interest requirements.
   *
   * @return true if the specified componentIds matches the area of interest requirements.
   */
  public boolean matches( @Nonnull final BitSet componentIds )
  {
    // This can be implemented MUCH more efficiently by manipulating the underlying words.
    // We should add a containsAll() method to out BitSet implementation
    int current = -1;
    while ( -1 != ( current = _and.nextSetBit( current + 1 ) ) )
    {
      if ( !componentIds.get( current ) )
      {
        return false;
      }
    }

    return ( _or.isEmpty() || _or.intersects( componentIds ) ) &&
           ( _not.isEmpty() || !_not.intersects( componentIds ) );
  }
}
