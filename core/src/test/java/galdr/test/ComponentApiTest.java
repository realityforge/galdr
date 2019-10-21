package galdr.test;

import galdr.AbstractTest;
import galdr.ComponentAPI;
import galdr.ComponentStorage;
import galdr.World;
import galdr.Worlds;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComponentApiTest
  extends AbstractTest
{
  private static class Health
  {
    int healthPoints;
  }

  private static class MyFlag
  {
  }

  @Test
  public void basicOperation()
  {
    final World world = Worlds.world().component( Health.class, Health::new ).build();

    final int entityId = createEntity( world );

    final ComponentAPI<Health> api = world.getComponentByType( Health.class );

    assertEquals( api.getId(), 0 );
    assertEquals( api.getStorage(), ComponentStorage.ARRAY );

    run( world, () -> assertFalse( api.has( entityId ) ) );
    run( world, () -> assertNull( api.find( entityId ) ) );

    final Health health = run( world, () -> api.create( entityId ) );

    final int healthPoints = randomInt( 100 );
    health.healthPoints = healthPoints;

    run( world, () -> assertTrue( api.has( entityId ) ) );
    run( world, () -> assertEquals( api.find( entityId ), health ) );
    run( world, () -> assertEquals( api.findOrCreate( entityId ), health ) );
    run( world, () -> assertEquals( api.get( entityId ).healthPoints, healthPoints ) );

    run( world, () -> api.remove( entityId ) );

    run( world, () -> assertFalse( api.has( entityId ) ) );
    run( world, () -> assertNull( api.find( entityId ) ) );
  }

  @Test
  public void basicOperationUsingAllocate()
  {
    final World world = Worlds.world().component( Health.class, Health::new ).build();

    final int entityId = createEntity( world );

    final ComponentAPI<Health> api = world.getComponentByType( Health.class );

    assertEquals( api.getId(), 0 );

    run( world, () -> assertFalse( api.has( entityId ) ) );
    run( world, () -> assertNull( api.find( entityId ) ) );

    run( world, () -> api.allocate( entityId ) );

    run( world, () -> assertTrue( api.has( entityId ) ) );
    run( world, () -> assertNotNull( api.find( entityId ) ) );

    final Health health = run( world, () -> api.get( entityId ) );

    final int healthPoints = randomInt( 100 );
    health.healthPoints = healthPoints;

    run( world, () -> assertTrue( api.has( entityId ) ) );
    run( world, () -> assertEquals( api.find( entityId ), health ) );
    run( world, () -> assertEquals( api.findOrCreate( entityId ), health ) );
    run( world, () -> assertEquals( api.get( entityId ).healthPoints, healthPoints ) );

    run( world, () -> api.remove( entityId ) );

    run( world, () -> assertFalse( api.has( entityId ) ) );
    run( world, () -> assertNull( api.find( entityId ) ) );
  }

  @Test
  public void basicOperationUsingFlagComponent()
  {
    final World world = Worlds.world().component( MyFlag.class ).build();

    final ComponentAPI<MyFlag> api = world.getComponentByType( MyFlag.class );
    assertEquals( api.getId(), 0 );

    final int entityId = createEntity( world );

    run( world, () -> assertFalse( api.has( entityId ) ) );
    assertEquals( api.getStorage(), ComponentStorage.NONE );

    run( world, () -> api.allocate( entityId ) );

    run( world, () -> assertTrue( api.has( entityId ) ) );

    run( world, () -> assertTrue( api.has( entityId ) ) );

    run( world, () -> api.remove( entityId ) );

    run( world, () -> assertFalse( api.has( entityId ) ) );
  }

  @Test
  public void attemptToInvokeInOtherWrongWorld()
  {
    final World world1 = Worlds.world().component( MyFlag.class ).build();
    final World world2 = Worlds.world().component( MyFlag.class ).build();

    final int entityId1 = createEntity( world1 );
    createEntity( world2 );

    final ComponentAPI<MyFlag> api = world1.getComponentByType( MyFlag.class );

    assertInvariantFailure( () -> run( world2, () -> api.allocate( entityId1 ) ),
                            "Galdr-0035: ComponentAPI method invoked in the context of the world 'World@2' but the component belongs to the world 'World@1'" );
  }
}
