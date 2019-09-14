package galdr;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class FastArrayComponentStoreTest
  extends AbstractTest
{
  private static class Component1
  {
  }

  @Test
  public void basicOperation()
  {
    final ComponentType<Component1> componentType = new ComponentType<>( Component1.class, Component1::new );

    final FastArrayComponentStore<Component1> componentStore = new FastArrayComponentStore<>( componentType, 5 );

    assertEquals( componentStore.getComponentType(), componentType );
    assertEquals( componentStore.getName(), componentType.getName() );
    assertEquals( componentStore.capacity(), 5 );

    final int entityId = 9;

    // entityId is > initial capacity
    assertFalse( componentStore.has( entityId ) );
    assertNull( componentStore.find( entityId ) );

    final Component1 component = componentStore.create( entityId );
    assertNotNull( component );

    assertEquals( componentStore.capacity(), 13 );

    assertTrue( componentStore.has( entityId ) );
    assertEquals( componentStore.find( entityId ), component );
    assertEquals( componentStore.get( entityId ), component );

    componentStore.remove( entityId );

    assertFalse( componentStore.has( entityId ) );
    assertNull( componentStore.find( entityId ) );

    // Capacity is not dminished after remove
    assertEquals( componentStore.capacity(), 13 );
  }
}
