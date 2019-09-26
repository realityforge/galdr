package galdr.spy;

import galdr.AbstractTest;
import galdr.World;
import galdr.Worlds;
import java.util.HashMap;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class EntityAddStartEventTest
  extends AbstractTest
{
  @Test
  public void basicOperation()
  {
    final World world = Worlds.world().build();
    final EntityAddStartEvent event = new EntityAddStartEvent( world );

    assertEquals( event.getWorld(), world );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "EntityAddStart" );
    assertEquals( data.get( "world" ), world.getName() );
    assertEquals( data.size(), 2 );
  }
}
