package galdr;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class NoStorageComponentManagerTest
  extends AbstractTest
{
  private static class Component1
  {
  }

  @Test
  public void basicOperation()
  {
    final World world = Worlds.world().component( Component1.class ).build();

    final NoStorageComponentManager<Component1> componentManager = (NoStorageComponentManager<Component1>)
      world.getComponentManagerByType( Component1.class );

    assertEquals( componentManager.getWorld(), world );
    assertEquals( componentManager.getId(), 0 );
    assertEquals( componentManager.getType(), Component1.class );
    assertEquals( componentManager.getName(), "Component1" );
    assertEquals( componentManager.toString(), "ComponentManager[Component1=0]" );
    assertEquals( componentManager.hashCode(), 0 );
    assertEquals( componentManager.getStorage(), ComponentStorage.NONE );

    final int entityId = createEntity( world );

    assertFalse( componentManager.has( entityId ) );
    assertNull( componentManager.find( entityId ) );

    componentManager.allocate( entityId );

    assertTrue( componentManager.has( entityId ) );

    componentManager.remove( entityId );

    assertFalse( componentManager.has( entityId ) );
    assertNull( componentManager.find( entityId ) );
  }

  @Test
  public void create_generatesError()
  {
    final World world = Worlds.world().component( Component1.class ).build();

    final NoStorageComponentManager<Component1> cm = (NoStorageComponentManager<Component1>)
      world.getComponentManagerByType( Component1.class );

    final int entityId = createEntity( world );

    assertFalse( cm.has( entityId ) );
    assertNull( cm.find( entityId ) );

    assertInvariantFailure( () -> cm.performCreate( entityId ),
                            "Galdr-0014: The ComponentManager.create() method has been invoked for the component named 'Component1' but the component was registered with ComponentStorage.NONE storage strategy and thus should never be accessed." );
  }

  @Test
  public void create_generatesError_eventWhenInvariantsDisabled()
  {
    GaldrTestUtil.noCheckInvariants();
    GaldrTestUtil.noCheckApiInvariants();

    final World world = Worlds.world().component( Component1.class ).build();

    final NoStorageComponentManager<Component1> cm = (NoStorageComponentManager<Component1>)
      world.getComponentManagerByType( Component1.class );

    final int entityId = createEntity( world );

    assertFalse( cm.has( entityId ) );
    assertNull( cm.find( entityId ) );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> cm.performCreate( entityId ) );

    assertNull( exception.getMessage() );
  }

  @Test
  public void get_generatesError()
  {
    final World world = Worlds.world().component( Component1.class ).build();

    final ComponentAPI<Component1> componentManager = world.getComponentByType( Component1.class );

    final int entityId = createEntity( world );

    assertFalse( run( world, () -> componentManager.has( entityId ) ) );
    assertNull( run( world, () -> componentManager.find( entityId ) ) );

    run( world, () -> componentManager.allocate( entityId ) );

    assertTrue( run( world, () -> componentManager.has( entityId ) ) );

    assertInvariantFailure( () -> run( world, () -> componentManager.get( entityId ) ),
                            "Galdr-0013: The ComponentManager.get() method has been invoked for the component named 'Component1' but the component was registered with ComponentStorage.NONE storage strategy and thus should never be accessed." );
  }

  @Test
  public void get_generatesError_evenWhenInvariantsDisabled()
  {
    GaldrTestUtil.noCheckInvariants();
    GaldrTestUtil.noCheckApiInvariants();

    final World world = Worlds.world().component( Component1.class ).build();

    final ComponentAPI<Component1> componentManager = world.getComponentByType( Component1.class );

    final int entityId = createEntity( world );

    assertFalse( run( world, () -> componentManager.has( entityId ) ) );
    assertNull( run( world, () -> componentManager.find( entityId ) ) );

    run( world, () -> componentManager.allocate( entityId ) );

    assertTrue( run( world, () -> componentManager.has( entityId ) ) );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> run( world, () -> componentManager.get( entityId ) ) );

    assertNull( exception.getMessage() );
  }
}
