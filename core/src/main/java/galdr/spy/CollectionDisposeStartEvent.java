package galdr.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * Notification emitted before a collection is disposed.
 */
public final class CollectionDisposeStartEvent
  implements SerializableEvent
{
  /**
   * The collection.
   */
  @Nonnull
  private final CollectionInfo _collection;

  public CollectionDisposeStartEvent( @Nonnull final CollectionInfo collection )
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
    map.put( "type", "CollectionDisposeStart" );
    // AreaOfInterest will need to be serialized in the future
    map.put( "areaOfInterest", _collection.getAreaOfInterest() );
  }
}
