package galdr.spy;

import galdr.AbstractTest;
import galdr.World;
import galdr.Worlds;
import java.util.Collections;
import java.util.HashMap;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class SubscriptionCreateCompleteEventTest
  extends AbstractTest
{
  private static class Health
  {
  }

  @Test
  public void basicOperation()
  {
    final World world = Worlds.world().component( Health.class ).build();
    final SubscriptionInfo subscription =
      world.getSpy().asSubscriptionInfo( createSubscription( world, Collections.singletonList( Health.class ) ) );
    final SubscriptionCreateCompleteEvent event = new SubscriptionCreateCompleteEvent( subscription );

    assertEquals( event.getSubscription(), subscription );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "SubscriptionCreateComplete" );
    assertEquals( data.get( "id" ), subscription.getId() );
    assertEquals( data.get( "name" ), subscription.getName() );
    assertEquals( data.get( "areaOfInterest" ), subscription.getCollection().getAreaOfInterest() );
    assertEquals( data.size(), 4 );
  }
}
