package galdr;

import java.util.BitSet;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * Represents a group of components that are of interest.
 * Typically this is used by a {@link galdr.Processor} to describe the {@link galdr.Entity} instances
 * that will be selected by the processor for processing.
 */
final class AreaOfInterest
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
   * @param componentIds the componentIds to test.
   * @return true if the specified componentIds matches the area of interest requirements.
   */
  boolean matches( @Nonnull final BitSet componentIds )
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

  @Nonnull
  BitSet getAll()
  {
    return _all;
  }

  @Nonnull
  BitSet getOne()
  {
    return _one;
  }

  @Nonnull
  BitSet getExclude()
  {
    return _exclude;
  }

  @Override
  public boolean equals( final Object obj )
  {
    if ( obj instanceof AreaOfInterest )
    {
      final AreaOfInterest other = (AreaOfInterest) obj;
      return other._all.equals( _all ) && other._one.equals( _one ) && other._exclude.equals( _exclude );
    }
    else
    {
      return false;
    }
  }

  @Override
  public int hashCode()
  {
    return Objects.hash( _all.hashCode(), _one.hashCode(), _exclude.hashCode() );
  }
}
