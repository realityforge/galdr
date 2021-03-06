package galdr;

import java.util.function.Supplier;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class MapComponentManagerTest
  extends AbstractTest
{
  private static class Component1
  {
  }

  @Test
  public void basicOperation()
  {
    final Supplier<Component1> createFn = Component1::new;
    final World world = Worlds.world()
      .initialEntityCount( 5 )
      .component( Component1.class, createFn, ComponentStorage.MAP )
      .build();

    final MapComponentManager<Component1> componentManager = (MapComponentManager<Component1>)
      world.getComponentManagerByType( Component1.class );

    assertEquals( componentManager.getWorld(), world );
    assertEquals( componentManager.getId(), 0 );
    assertEquals( componentManager.getType(), Component1.class );
    assertEquals( componentManager.getCreateFn(), createFn );
    assertEquals( componentManager.getName(), "Component1" );
    assertEquals( componentManager.toString(), "ComponentManager[Component1=0]" );
    assertEquals( componentManager.hashCode(), 0 );
    assertEquals( componentManager.getStorage(), ComponentStorage.MAP );

    assertEquals( componentManager.getComponents().size(), 0 );

    createEntity( world );
    createEntity( world );
    createEntity( world );
    createEntity( world );
    createEntity( world );
    createEntity( world );
    createEntity( world );
    createEntity( world );

    final int entityId = createEntity( world );

    run( world, () -> assertFalse( componentManager.has( entityId ) ) );
    run( world, () -> assertNull( componentManager.find( entityId ) ) );

    assertEquals( componentManager.getComponents().size(), 0 );

    final Component1 component = run( world, () -> componentManager.create( entityId ) );
    assertNotNull( component );

    assertEquals( componentManager.getComponents().size(), 1 );

    run( world, () -> assertTrue( componentManager.has( entityId ) ) );
    run( world, () -> assertEquals( componentManager.find( entityId ), component ) );
    run( world, () -> assertEquals( componentManager.get( entityId ), component ) );

    run( world, () -> componentManager.remove( entityId ) );

    assertEquals( componentManager.getComponents().size(), 0 );

    run( world, () -> assertFalse( componentManager.has( entityId ) ) );
    run( world, () -> assertNull( componentManager.find( entityId ) ) );
  }
}
