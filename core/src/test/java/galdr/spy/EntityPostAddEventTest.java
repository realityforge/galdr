package galdr.spy;

import galdr.AbstractTest;
import galdr.Galdr;
import galdr.World;
import java.util.BitSet;
import java.util.HashMap;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class EntityPostAddEventTest
  extends AbstractTest
{
  @Test
  public void basicOperation()
  {
    final World world = Galdr.world().build();
    final int entityId = world.getEntityAPI().newEntity( new BitSet() );
    final EntityPostAddEvent event = new EntityPostAddEvent( world, entityId );

    assertEquals( event.getWorld(), world );
    assertEquals( event.getEntityId(), entityId );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "EntityPostAdd" );
    assertEquals( data.get( "world" ), world.getName() );
    assertEquals( data.get( "entityId" ), entityId );
    assertEquals( data.size(), 3 );
  }
}
