package galdr.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * Notification emitted after a collection is disposed.
 */
public final class CollectionDisposeCompleteEvent
  implements SerializableEvent
{
  /**
   * The collection.
   */
  @Nonnull
  private final CollectionInfo _collection;

  public CollectionDisposeCompleteEvent( @Nonnull final CollectionInfo collection )
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
    map.put( "type", "CollectionDisposeComplete" );
    // AreaOfInterest will need to be serialized in the future
    map.put( "areaOfInterest", _collection.getAreaOfInterest() );
  }
}
