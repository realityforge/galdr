package galdr.spy;

import galdr.AbstractTest;
import galdr.AreaOfInterest;
import galdr.World;
import galdr.Worlds;
import java.util.Collections;
import java.util.HashMap;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class SubscriptionDisposeStartEventTest
  extends AbstractTest
{
  private static class Health
  {
  }

  @Test
  public void basicOperation()
  {
    final World world = Worlds.world().component( Health.class ).build();
    final int id = randomInt( 100 );
    final String name = randomString();
    final AreaOfInterest areaOfInterest = world.createAreaOfInterest( Collections.singletonList( Health.class ) );
    final SubscriptionDisposeStartEvent event =
      new SubscriptionDisposeStartEvent( id, name, areaOfInterest );

    assertEquals( event.getId(), id );
    assertEquals( event.getName(), name );
    assertEquals( event.getAreaOfInterest(), areaOfInterest );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "SubscriptionDisposeStart" );
    assertEquals( data.get( "id" ), id );
    assertEquals( data.get( "name" ), name );
    assertEquals( data.get( "areaOfInterest" ), areaOfInterest );
    assertEquals( data.size(), 4 );
  }
}
