package galdr.spy;

import galdr.AreaOfInterest;
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
   * The id of the subscription.
   */
  private final int _id;
  /**
   * The name of the subscription.
   */
  @Nonnull
  private final String _name;
  /**
   * The area of interest of the subscription.
   */
  @Nonnull
  private final AreaOfInterest _areaOfInterest;

  public SubscriptionDisposeStartEvent( final int id,
                                        @Nonnull final String name,
                                        @Nonnull final AreaOfInterest areaOfInterest )
  {
    _id = id;
    _name = Objects.requireNonNull( name );
    _areaOfInterest = Objects.requireNonNull( areaOfInterest );
  }

  public int getId()
  {
    return _id;
  }

  @Nonnull
  public String getName()
  {
    return _name;
  }

  @Nonnull
  public AreaOfInterest getAreaOfInterest()
  {
    return _areaOfInterest;
  }

  @Override
  public void toMap( @Nonnull final Map<String, Object> map )
  {
    map.put( "type", "SubscriptionDisposeStart" );
    map.put( "id", _id );
    map.put( "name", _name );
    // AreaOfInterest will need to be serialized in the future
    map.put( "areaOfInterest", _areaOfInterest );
  }
}
