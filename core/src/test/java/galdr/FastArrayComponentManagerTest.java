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
    final FastArrayComponentManager<Component1> componentManager =
      new FastArrayComponentManager<>( 23, Component1.class, createFn, 5 );

    assertEquals( componentManager.getIndex(), 23 );

    assertEquals( componentManager.getType(), Component1.class );
    assertEquals( componentManager.getCreateFn(), createFn );
    assertEquals( componentManager.getName(), "Component1" );
    assertEquals( componentManager.toString(), "ComponentManager[Component1=23]" );
    assertEquals( componentManager.hashCode(), 23 );
    assertEquals( componentManager.capacity(), 5 );

    final int entityId = 9;

    // entityId is > initial capacity
    assertFalse( componentManager.has( entityId ) );
    assertNull( componentManager.find( entityId ) );

    final Component1 component = componentManager.create( entityId );
    assertNotNull( component );

    assertEquals( componentManager.capacity(), 13 );

    assertTrue( componentManager.has( entityId ) );
    assertEquals( componentManager.find( entityId ), component );
    assertEquals( componentManager.get( entityId ), component );

    componentManager.remove( entityId );

    assertFalse( componentManager.has( entityId ) );
    assertNull( componentManager.find( entityId ) );

    // Capacity is not dminished after remove
    assertEquals( componentManager.capacity(), 13 );
  }
}
