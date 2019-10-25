package galdr;

import grim.annotations.OmitSymbol;
import java.util.BitSet;
import java.util.Objects;
import javax.annotation.Nonnull;
import static org.realityforge.braincheck.Guards.*;

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
  private final ComponentIdSet _all;
  /**
   * Components that an entity MUST have at least one of to be matched.
   */
  @Nonnull
  private final ComponentIdSet _one;
  /**
   * Components that an entity MUST NOT have to be matched.
   */
  @Nonnull
  private final ComponentIdSet _exclude;

  AreaOfInterest( @Nonnull final ComponentIdSet all,
                  @Nonnull final ComponentIdSet one,
                  @Nonnull final ComponentIdSet exclude )
  {
    if ( Galdr.shouldCheckInvariants() )
    {
      invariant( () -> !all.getBitSet().intersects( one.getBitSet() ),
                 () -> "Galdr-0005: AreaOfInterest passed intersecting BitSets " +
                       "all (" + all.getBitSet() + ") and one (" + one.getBitSet() + ")." );
      invariant( () -> !all.getBitSet().intersects( exclude.getBitSet() ),
                 () -> "Galdr-0005: AreaOfInterest passed intersecting BitSets " +
                       "all (" + all.getBitSet() + ") and exclude (" + exclude.getBitSet() + ")." );
      invariant( () -> !one.getBitSet().intersects( exclude.getBitSet() ),
                 () -> "Galdr-0005: AreaOfInterest passed intersecting BitSets " +
                       "one (" + one.getBitSet() + ") and exclude (" + exclude.getBitSet() + ")." );
    }
    _all = Objects.requireNonNull( all );
    _one = Objects.requireNonNull( one );
    _exclude = Objects.requireNonNull( exclude );
  }

  /**
   * Return true if the specified componentIds matches the area of interest requirements.
   *
   * @param componentIds the componentIds to test.
   * @return true if the specified componentIds matches the area of interest requirements.
   */
  boolean matches( @Nonnull final BitSet componentIds )
  {
    final BitSet all = _all.getBitSet();
    // This can be implemented MUCH more efficiently by manipulating the underlying words.
    // We should add a containsAll() method to out BitSet implementation
    int current = -1;
    while ( -1 != ( current = all.nextSetBit( current + 1 ) ) )
    {
      if ( !componentIds.get( current ) )
      {
        return false;
      }
    }

    return ( _one.getBitSet().isEmpty() || _one.getBitSet().intersects( componentIds ) ) &&
           ( _exclude.getBitSet().isEmpty() || !_exclude.getBitSet().intersects( componentIds ) );
  }

  @Nonnull
  ComponentIdSet getAll()
  {
    return _all;
  }

  @Nonnull
  ComponentIdSet getOne()
  {
    return _one;
  }

  @Nonnull
  ComponentIdSet getExclude()
  {
    return _exclude;
  }

  @OmitSymbol( unless = "galdr.debug_to_string" )
  @Override
  public String toString()
  {
    if ( Galdr.areDebugToStringMethodsEnabled() )
    {
      return "AreaOfInterest[" +
             "all=" + getAll().getBitSet() + "," +
             "one=" + getOne().getBitSet() + "," +
             "exclude=" + getExclude().getBitSet() +
             "]";
    }
    else
    {
      return super.toString();
    }
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
