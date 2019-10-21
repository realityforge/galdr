package galdr;

import galdr.spy.WorldInfo;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class WorldInfoImplTest
  extends AbstractTest
{
  private static class Component1
  {
  }

  private static class Component2
  {
  }

  @Test
  public void basicOperation()
  {
    final String name = randomString();
    final int initialEntityCount = randomInt( 200 ) + 10;
    final World world = Worlds.world( name ).initialEntityCount( initialEntityCount ).build();
    final WorldInfo info = world.getSpy().asWorldInfo();

    assertEquals( info.getName(), name );
    assertEquals( info.getEntityCount(), 0 );
    assertEquals( info.getEntityCapacity(), initialEntityCount );
    assertEquals( info.toString(), world.toString() );

    // This verifies that it is exactly the same instance
    //noinspection SimplifiedTestNGAssertion
    assertTrue( info == world.getSpy().asWorldInfo() );
  }

  @Test
  public void getEntityCount()
  {
    final World world = Worlds.world().build();
    final WorldInfo info = world.getSpy().asWorldInfo();

    assertEquals( info.getEntityCount(), 0 );

    final int entityId1 = createEntity( world );
    final int entityId2 = createEntity( world );
    final int entityId3 = createEntity( world );

    assertEquals( info.getEntityCount(), 3 );

    run( world, () -> world.disposeEntity( entityId1 ) );

    assertEquals( info.getEntityCount(), 2 );

    createEntity( world );
    createEntity( world );

    assertEquals( info.getEntityCount(), 4 );

    run( world, () -> world.disposeEntity( entityId2 ) );
    run( world, () -> world.disposeEntity( entityId3 ) );

    assertEquals( info.getEntityCount(), 2 );
  }

  @Test
  public void getComponentCount()
  {
    final World world1 = Worlds.world().build();
    final World world2 = Worlds.world().component( Component1.class ).build();
    final World world3 = Worlds.world().component( Component1.class ).component( Component2.class ).build();

    assertEquals( world1.getSpy().asWorldInfo().getComponentCount(), 0 );
    assertEquals( world2.getSpy().asWorldInfo().getComponentCount(), 1 );
    assertEquals( world3.getSpy().asWorldInfo().getComponentCount(), 2 );
  }

  @Test
  public void equalsAndHashCode()
  {
    final World world1 = Worlds.world().build();
    final WorldInfo info1 = world1.getSpy().asWorldInfo();
    final WorldInfo info1b = new WorldInfoImpl( world1 );
    final WorldInfo info2 = Worlds.world().build().getSpy().asWorldInfo();

    assertEquals( info1, info1b );
    assertNotEquals( info1, info2 );
    assertEquals( info1b, info1 );
    assertNotEquals( info1b, info2 );
    assertNotEquals( info2, info1 );
    assertNotEquals( info2, info1b );
    assertNotEquals( info1, "X" );

    assertEquals( info1.hashCode(), info1b.hashCode() );
    assertNotEquals( info1.hashCode(), info2.hashCode() );
    assertEquals( info1b.hashCode(), info1.hashCode() );
    assertNotEquals( info1b.hashCode(), info2.hashCode() );
    assertNotEquals( info2.hashCode(), info1.hashCode() );
    assertNotEquals( info2.hashCode(), info1b.hashCode() );
  }

  @Test
  public void spyDisabled()
  {
    GaldrTestUtil.disableSpies();

    final World world = Worlds.world().build();
    assertInvariantFailure( world::asInfo,
                            "Galdr-0040: World.asInfo() invoked but Galdr.areSpiesEnabled() returned false." );
  }
}
