package galdr;

import galdr.spy.CollectionInfo;
import grim.annotations.OmitType;
import java.util.Objects;
import javax.annotation.Nonnull;

@OmitType( unless = "galdr.enable_spies" )
final class CollectionInfoImpl
  implements CollectionInfo
{
  @Nonnull
  private final EntityCollection _entityCollection;

  CollectionInfoImpl( @Nonnull final EntityCollection entityCollection )
  {
    _entityCollection = Objects.requireNonNull( entityCollection );
  }

  @Override
  public boolean isDisposed()
  {
    return _entityCollection.isDisposed();
  }

  @Nonnull
  @Override
  public AreaOfInterest getAreaOfInterest()
  {
    return _entityCollection.getAreaOfInterest();
  }

  @Override
  public int getSubscriptionCount()
  {
    return _entityCollection.getRefCount();
  }

  @Override
  public int getEntityCount()
  {
    return _entityCollection.getEntities().cardinality();
  }

  @Override
  public String toString()
  {
    return _entityCollection.toString();
  }

  @Override
  public boolean equals( final Object o )
  {
    if ( o instanceof CollectionInfoImpl )
    {
      final CollectionInfoImpl that = (CollectionInfoImpl) o;
      return _entityCollection.equals( that._entityCollection );
    }
    else
    {
      return false;
    }
  }

  @Override
  public int hashCode()
  {
    return _entityCollection.hashCode();
  }
}
