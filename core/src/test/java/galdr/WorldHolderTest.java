package galdr;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class WorldHolderTest
  extends AbstractTest
{
  @Test
  public void isActive()
  {
    assertFalse( WorldHolder.isActive() );

    final WorldBuilder builder = Galdr.world();
    assertTrue( WorldHolder.isActive() );

    // Complete build of world and thus deactivate
    builder.build();

    assertFalse( WorldHolder.isActive() );
  }

  @Test
  public void world()
  {
    assertFalse( WorldHolder.isActive() );

    final WorldBuilder builder = Galdr.world();

    final World world = WorldHolder.world();
    assertNotNull( world );

    final World createdWorld = builder.build();

    assertEquals( createdWorld, world );
  }

  @Test
  public void world_onInactive()
  {
    assertInvariantFailure( WorldHolder::world, "Galdr-0026: Invoked WorldHolder.world() when no world was active." );
  }

  @Test
  public void activateAndDeactivateWorld()
  {
    final World world = Galdr.world().build();

    assertFalse( WorldHolder.isActive() );
    WorldHolder.activateWorld( world );
    assertTrue( WorldHolder.isActive() );

    assertEquals( WorldHolder.world(), world );

    WorldHolder.deactivateWorld( world );

    assertFalse( WorldHolder.isActive() );
  }

  @Test
  public void activateWorld_whenWorldActive()
  {
    final World world1 = Galdr.world().build();
    final World world2 = Galdr.world().build();

    assertFalse( WorldHolder.isActive() );
    WorldHolder.activateWorld( world1 );
    assertTrue( WorldHolder.isActive() );

    assertInvariantFailure( () -> WorldHolder.activateWorld( world2 ),
                            "Galdr-0024: Invoked WorldHolder.activateWorld() with world named 'World@2' but an existing world named 'World@1' is active." );
  }

  @Test
  public void deactivateWorld_whenWorldNotActive()
  {
    final World world1 = Galdr.world().build();
    final World world2 = Galdr.world().build();

    assertFalse( WorldHolder.isActive() );
    WorldHolder.activateWorld( world1 );
    assertTrue( WorldHolder.isActive() );

    assertInvariantFailure( () -> WorldHolder.deactivateWorld( world2 ),
                            "Galdr-0026: Attempted to deactivate world named 'World@2' that is not active." );

    WorldHolder.deactivateWorld( world1 );

    assertInvariantFailure( () -> WorldHolder.deactivateWorld( world1 ),
                            "Galdr-0026: Attempted to deactivate world named 'World@1' that is not active." );
  }
}
