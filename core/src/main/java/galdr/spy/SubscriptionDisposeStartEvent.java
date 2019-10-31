package galdr.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * Notification emitted before a subscription is disposed.
 */
public final class SubscriptionDisposeStartEvent
  implements SerializableEvent
{
  /**
   * The subscription.
   */
  @Nonnull
  private final SubscriptionInfo _subscription;

  public SubscriptionDisposeStartEvent( @Nonnull final SubscriptionInfo subscription )
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
    map.put( "type", "SubscriptionDisposeStart" );
    map.put( "id", getSubscription().getId() );
    map.put( "name", getSubscription().getName() );
    // AreaOfInterest will need to be serialized in the future
    map.put( "areaOfInterest", getSubscription().getCollection().getAreaOfInterest() );
  }
}
