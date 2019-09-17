package galdr.test;

import galdr.AbstractTest;
import galdr.ComponentAPI;
import galdr.Galdr;
import galdr.World;
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
    final World world = Galdr.world().component( Health.class, Health::new ).build();

    final ComponentAPI<Health> api = world.getComponentByType( Health.class );

    assertFalse( api.has( 23 ) );
    assertNull( api.find( 23 ) );

    final Health health = api.create( 23 );

    final int healthPoints = randomInt( 100 );
    health.healthPoints = healthPoints;

    assertTrue( api.has( 23 ) );
    assertEquals( api.find( 23 ), health );
    assertEquals( api.findOrCreate( 23 ), health );
    assertEquals( api.get( 23 ).healthPoints, healthPoints );

    api.remove( 23 );

    assertFalse( api.has( 23 ) );
    assertNull( api.find( 23 ) );
  }
}
