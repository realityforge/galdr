package galdr.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * Notification emitted when a subscription is attached to an existing collection.
 */
public final class CollectionAttachEvent
  implements SerializableEvent
{
  /**
   * The subscription.
   */
  @Nonnull
  private final SubscriptionInfo _subscription;

  public CollectionAttachEvent( @Nonnull final SubscriptionInfo subscription )
  {
    _subscription = Objects.requireNonNull( subscription );
  }

  @Nonnull
  public SubscriptionInfo getSubscription()
  {
    return _subscription;
  }

  @Override
  public void toMap( @Nonnull final Map<String, Object> map )
  {
    map.put( "type", "CollectionAttach" );
    map.put( "id", _subscription.getId() );
    map.put( "name", _subscription.getName() );
    // AreaOfInterest will need to be serialized in the future
    map.put( "areaOfInterest", _subscription.getCollection().getAreaOfInterest() );
  }
}
