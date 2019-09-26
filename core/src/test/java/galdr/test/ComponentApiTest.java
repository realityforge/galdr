package galdr.test;

import galdr.AbstractTest;
import galdr.ComponentAPI;
import galdr.World;
import galdr.Worlds;
import java.util.BitSet;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComponentApiTest
  extends AbstractTest
{
  private static class Health
  {
    int healthPoints;
  }

  @Test
  public void basicOperation()
  {
    final World world = Worlds.world().component( Health.class, Health::new ).build();

    final int entityId = world.createEntity( new BitSet() );

    final ComponentAPI<Health> api = world.getComponentByType( Health.class );

    assertEquals( api.getId(), 0 );

    assertFalse( api.has( entityId ) );
    assertNull( api.find( entityId ) );

    final Health health = api.create( entityId );

    final int healthPoints = randomInt( 100 );
    health.healthPoints = healthPoints;

    assertTrue( api.has( entityId ) );
    assertEquals( api.find( entityId ), health );
    assertEquals( api.findOrCreate( entityId ), health );
    assertEquals( api.get( entityId ).healthPoints, healthPoints );

    api.remove( entityId );

    assertFalse( api.has( entityId ) );
    assertNull( api.find( entityId ) );
  }
}
