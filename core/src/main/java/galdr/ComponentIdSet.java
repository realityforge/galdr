package galdr;

import java.util.BitSet;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * A set of component ids.
 */
public final class ComponentIdSet
{
  /**
   * The underlying bit set of component ids.
   */
  @Nonnull
  private final BitSet _componentIds;

  ComponentIdSet( @Nonnull final BitSet componentIds )
  {
    _componentIds = Objects.requireNonNull( componentIds );
  }

  /**
   * Return the underlying BitSet.
   * This is not part of the public API to avoid exposing a mutable value that
   * an end user can modify without going through a public API.
   *
   * @return the underlying BitSet.
   */
  @Nonnull
  BitSet getComponentIds()
  {
    return _componentIds;
  }

  @Override
  public int hashCode()
  {
    return _componentIds.hashCode();
  }

  @Override
  public boolean equals( final Object obj )
  {
    return obj instanceof ComponentIdSet && ( (ComponentIdSet) obj )._componentIds.equals( _componentIds );
  }

  @Override
  public String toString()
  {
    if ( Galdr.areDebugToStringMethodsEnabled() )
    {
      final String bitSet = _componentIds.toString();
      return "ComponentIdSet[" + bitSet.substring( 1, bitSet.length() - 1 ) + "]";
    }
    else
    {
      return super.toString();
    }
  }
}
