package galdr.spy;

import galdr.AbstractTest;
import galdr.Galdr;
import galdr.World;
import java.util.BitSet;
import java.util.HashMap;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class LinkAddCompleteEventTest
  extends AbstractTest
{
  @Test
  public void basicOperation()
  {
    final World world = Galdr.world().build();
    final int entity1Id = world.createEntity( new BitSet() );
    final int entity2Id = world.createEntity( new BitSet() );
    final LinkAddCompleteEvent event = new LinkAddCompleteEvent( world, entity1Id, entity2Id );

    assertEquals( event.getWorld(), world );
    assertEquals( event.getSourceEntityId(), entity1Id );
    assertEquals( event.getTargetEntityId(), entity2Id );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "LinkAddComplete" );
    assertEquals( data.get( "world" ), world.getName() );
    assertEquals( data.get( "sourceEntityId" ), entity1Id );
    assertEquals( data.get( "targetEntityId" ), entity2Id );
    assertEquals( data.size(), 4 );
  }
}
