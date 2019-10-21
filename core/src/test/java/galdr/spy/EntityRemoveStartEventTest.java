package galdr.spy;

import galdr.AbstractTest;
import galdr.World;
import galdr.Worlds;
import java.util.BitSet;
import java.util.HashMap;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class EntityRemoveStartEventTest
  extends AbstractTest
{
  @Test
  public void basicOperation()
  {
    final World world = Worlds.world().build();
    final int entityId = createEntity( world );
    final BitSet componentIds = set();
    final EntityRemoveStartEvent event = new EntityRemoveStartEvent( world, entityId, componentIds );

    assertEquals( event.getWorld(), world );
    assertEquals( event.getEntityId(), entityId );
    assertEquals( event.getComponentIds(), componentIds );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "EntityRemoveStart" );
    assertEquals( data.get( "world" ), world.getName() );
    assertEquals( data.get( "entityId" ), entityId );
    assertEquals( data.get( "componentIds" ), componentIds );
    assertEquals( data.size(), 4 );
  }
}
