package galdr;

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
    final FastArrayComponentManager<Component1> componentStore =
      new FastArrayComponentManager<>( Component1.class, createFn, 5 );

    assertEquals( componentStore.toString(), "ComponentManager[Component1=-1]" );
    componentStore.initIndex( 23 );
    assertEquals( componentStore.getIndex(), 23 );

    assertEquals( componentStore.getType(), Component1.class );
    assertEquals( componentStore.getCreateFn(), createFn );
    assertEquals( componentStore.getName(), "Component1" );
    assertEquals( componentStore.toString(), "ComponentManager[Component1=23]" );
    assertEquals( componentStore.hashCode(), 23 );
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
