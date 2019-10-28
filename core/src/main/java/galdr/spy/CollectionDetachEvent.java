package galdr.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * Notification emitted when a subscription is detached from an existing collection.
 */
public final class CollectionDetachEvent
  implements SerializableEvent
{
  /**
   * The collection.
   */
  @Nonnull
  private final CollectionInfo _collection;

  public CollectionDetachEvent( @Nonnull final CollectionInfo collection )
  {
    _collection = Objects.requireNonNull( collection );
  }

  @Nonnull
  public CollectionInfo getCollection()
  {
    return _collection;
  }

  @Override
  public void toMap( @Nonnull final Map<String, Object> map )
  {
    map.put( "type", "CollectionAttach" );
    // AreaOfInterest will need to be serialized in the future
    map.put( "areaOfInterest", _collection.getAreaOfInterest() );
  }
}
