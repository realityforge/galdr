package galdr.spy;

import galdr.AbstractTest;
import galdr.Galdr;
import galdr.World;
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
    final World world = Galdr.world().build();
    final int entityId = world.createEntity( new BitSet( 0 ) );
    final EntityRemoveStartEvent event = new EntityRemoveStartEvent( world, entityId );

    assertEquals( event.getWorld(), world );
    assertEquals( event.getEntityId(), entityId );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "EntityRemoveStart" );
    assertEquals( data.get( "world" ), world.getName() );
    assertEquals( data.get( "entityId" ), entityId );
    assertEquals( data.size(), 3 );
  }
}
