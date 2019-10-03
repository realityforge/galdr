package galdr.spy;

import galdr.AbstractTest;
import galdr.World;
import galdr.Worlds;
import java.util.HashMap;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComponentRemoveStartEventTest
  extends AbstractTest
{
  private static class Health
  {
  }

  @Test
  public void basicOperation()
  {
    final World world = Worlds.world().component( Health.class, Health::new ).build();
    final int entityId = createEntity( world, set() );
    final int componentId = world.getComponentByType( Health.class ).getId();
    final ComponentRemoveStartEvent event = new ComponentRemoveStartEvent( world, entityId, componentId );

    assertEquals( event.getWorld(), world );
    assertEquals( event.getEntityId(), entityId );
    assertEquals( event.getComponentId(), componentId );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "ComponentRemoveStart" );
    assertEquals( data.get( "world" ), world.getName() );
    assertEquals( data.get( "entityId" ), entityId );
    assertEquals( data.get( "componentId" ), componentId );
    assertEquals( data.size(), 4 );
  }
}
