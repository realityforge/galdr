package galdr.spy;

import galdr.AbstractTest;
import galdr.World;
import galdr.Worlds;
import java.util.BitSet;
import java.util.HashMap;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class LinkRemoveCompleteEventTest
  extends AbstractTest
{
  @Test
  public void basicOperation()
  {
    final World world = Worlds.world().build();
    final int entity1Id = world.createEntity( new BitSet() );
    final int entity2Id = world.createEntity( new BitSet() );
    final LinkRemoveCompleteEvent event = new LinkRemoveCompleteEvent( world, entity1Id, entity2Id );

    assertEquals( event.getWorld(), world );
    assertEquals( event.getSourceEntityId(), entity1Id );
    assertEquals( event.getTargetEntityId(), entity2Id );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "LinkRemoveComplete" );
    assertEquals( data.get( "world" ), world.getName() );
    assertEquals( data.get( "sourceEntityId" ), entity1Id );
    assertEquals( data.get( "targetEntityId" ), entity2Id );
    assertEquals( data.size(), 4 );
  }
}
