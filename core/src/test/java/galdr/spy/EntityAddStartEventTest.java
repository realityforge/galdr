package galdr.spy;

import galdr.AbstractTest;
import galdr.World;
import galdr.Worlds;
import java.util.BitSet;
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
    final BitSet componentIds = new BitSet();
    final EntityAddStartEvent event = new EntityAddStartEvent( world, componentIds );

    assertEquals( event.getWorld(), world );
    assertEquals( event.getComponentIds(), componentIds );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "EntityAddStart" );
    assertEquals( data.get( "world" ), world.getName() );
    assertEquals( data.get( "componentIds" ), componentIds );
    assertEquals( data.size(), 3 );
  }
}
