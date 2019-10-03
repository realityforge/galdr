package galdr;

import java.util.BitSet;
import java.util.function.Supplier;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class FastArrayComponentManagerTest
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
      .component( Component1.class, createFn )
      .build();

    final FastArrayComponentManager<Component1> componentManager = (FastArrayComponentManager<Component1>)
      world.getComponentManagerByType( Component1.class );

    assertEquals( componentManager.getWorld(), world );
    assertEquals( componentManager.getId(), 0 );
    assertEquals( componentManager.getType(), Component1.class );
    assertEquals( componentManager.getCreateFn(), createFn );
    assertEquals( componentManager.getName(), "Component1" );
    assertEquals( componentManager.toString(), "ComponentManager[Component1=0]" );
    assertEquals( componentManager.hashCode(), 0 );
    assertEquals( componentManager.getStorage(), ComponentStorage.ARRAY );
    assertEquals( componentManager.capacity(), 5 );

    createEntity( world, set() );
    createEntity( world, set() );
    createEntity( world, set() );
    createEntity( world, set() );
    createEntity( world, set() );
    createEntity( world, set() );
    createEntity( world, set() );
    createEntity( world, set() );

    final int entityId = createEntity( world, set() );

    // entityId is > initial capacity
    assertFalse( componentManager.has( entityId ) );
    assertNull( componentManager.find( entityId ) );

    final Component1 component = componentManager.create( entityId );
    assertNotNull( component );

    assertEquals( componentManager.capacity(), 12 );

    assertTrue( componentManager.has( entityId ) );
    assertEquals( componentManager.find( entityId ), component );
    assertEquals( componentManager.get( entityId ), component );

    componentManager.remove( entityId );

    assertFalse( componentManager.has( entityId ) );
    assertNull( componentManager.find( entityId ) );

    // Capacity is not diminished after remove
    assertEquals( componentManager.capacity(), 12 );
  }
}
