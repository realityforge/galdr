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
  /**
   * Components that an entity MUST have to be matched.
   */
  @Nonnull
  private final BitSet _all = new BitSet();
  /**
   * Components that an entity MUST have at least one of to be matched.
   */
  @Nonnull
  private final BitSet _one = new BitSet();
  /**
   * Components that an entity MUST NOT have to be matched.
   */
  @Nonnull
  private final BitSet _exclude = new BitSet();

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
    while ( -1 != ( current = _all.nextSetBit( current + 1 ) ) )
    {
      if ( !componentIds.get( current ) )
      {
        return false;
      }
    }

    return ( _one.isEmpty() || _one.intersects( componentIds ) ) &&
           ( _exclude.isEmpty() || !_exclude.intersects( componentIds ) );
  }
}
