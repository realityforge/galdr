package galdr.spy;

import galdr.AbstractTest;
import galdr.World;
import galdr.Worlds;
import java.util.HashMap;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class LinkAddStartEventTest
  extends AbstractTest
{
  @Test
  public void basicOperation()
  {
    final World world = Worlds.world().build();
    final int entity1Id = createEntity( world, set() );
    final int entity2Id = createEntity( world, set() );
    final LinkAddStartEvent event = new LinkAddStartEvent( world, entity1Id, entity2Id );

    assertEquals( event.getWorld(), world );
    assertEquals( event.getSourceEntityId(), entity1Id );
    assertEquals( event.getTargetEntityId(), entity2Id );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "LinkAddStart" );
    assertEquals( data.get( "world" ), world.getName() );
    assertEquals( data.get( "sourceEntityId" ), entity1Id );
    assertEquals( data.get( "targetEntityId" ), entity2Id );
    assertEquals( data.size(), 4 );
  }
}
