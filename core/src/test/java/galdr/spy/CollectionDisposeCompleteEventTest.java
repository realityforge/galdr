package galdr.spy;

import galdr.AbstractTest;
import galdr.AreaOfInterest;
import galdr.World;
import galdr.Worlds;
import java.util.Collections;
import java.util.HashMap;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class CollectionDisposeCompleteEventTest
  extends AbstractTest
{
  private static class Health
  {
  }

  @Test
  public void basicOperation()
  {
    final World world = Worlds.world().component( Health.class ).build();
    final AreaOfInterest areaOfInterest = world.createAreaOfInterest( Collections.singletonList( Health.class ) );
    run( world, () -> world.createSubscription( areaOfInterest ) );
    final CollectionInfo info = world.getSpy().getCollections().get( areaOfInterest );
    final CollectionDisposeCompleteEvent event = new CollectionDisposeCompleteEvent( info );

    assertEquals( event.getCollection(), info );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "CollectionDisposeComplete" );
    assertEquals( data.get( "areaOfInterest" ), areaOfInterest );
    assertEquals( data.size(), 2 );
  }
}
