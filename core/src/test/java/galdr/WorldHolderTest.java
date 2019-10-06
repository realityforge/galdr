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

    final Worlds.Builder builder = Worlds.world();
    assertTrue( WorldHolder.isActive() );

    // Complete build of world and thus deactivate
    builder.build();

    assertFalse( WorldHolder.isActive() );
  }

  @Test
  public void world()
  {
    assertFalse( WorldHolder.isActive() );

    final Worlds.Builder builder = Worlds.world();

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
    final World world = Worlds.world().build();

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
    final World world1 = Worlds.world().build();
    final World world2 = Worlds.world().build();

    assertFalse( WorldHolder.isActive() );
    WorldHolder.activateWorld( world1 );
    assertTrue( WorldHolder.isActive() );

    assertInvariantFailure( () -> WorldHolder.activateWorld( world2 ),
                            "Galdr-0024: Invoked WorldHolder.activateWorld() with world named 'World@2' but an existing world named 'World@1' is active." );
  }

  @SuppressWarnings( "ConstantConditions" )
  @Test
  public void activateWorld_nullWorld()
  {
    assertInvariantFailure( () -> WorldHolder.activateWorld( null ),
                            "Galdr-0023: Invoked WorldHolder.activateWorld() with null world." );
  }

  @Test
  public void deactivateWorld_whenWorldNotActive()
  {
    final World world1 = Worlds.world().build();
    final World world2 = Worlds.world().build();

    assertFalse( WorldHolder.isActive() );
    WorldHolder.activateWorld( world1 );
    assertTrue( WorldHolder.isActive() );

    assertInvariantFailure( () -> WorldHolder.deactivateWorld( world2 ),
                            "Galdr-0028: Attempted to deactivate world named 'World@2' that is not active." );

    WorldHolder.deactivateWorld( world1 );

    assertInvariantFailure( () -> WorldHolder.deactivateWorld( world1 ),
                            "Galdr-0028: Attempted to deactivate world named 'World@1' that is not active." );
  }

  @Test
  public void run_action()
  {
    final World world = Worlds.world().build();

    assertInvariantFailure( WorldHolder::world, "Galdr-0026: Invoked WorldHolder.world() when no world was active." );
    WorldHolder.run( world, () -> assertEquals( WorldHolder.world(), world ) );
    assertInvariantFailure( WorldHolder::world, "Galdr-0026: Invoked WorldHolder.world() when no world was active." );
  }

  @Test
  public void run_function()
  {
    final World world = Worlds.world().build();

    assertInvariantFailure( WorldHolder::world, "Galdr-0026: Invoked WorldHolder.world() when no world was active." );
    final int value = WorldHolder.run( world, () -> {
      assertEquals( WorldHolder.world(), world );
      return 111;
    } );
    assertInvariantFailure( WorldHolder::world, "Galdr-0026: Invoked WorldHolder.world() when no world was active." );
    assertEquals( value, 111 );
  }
}
