package galdr;

import java.util.function.Supplier;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComponentTypeTest
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
    final Supplier<Component1> createFn = Component1::new;
    final ComponentType<Component1> componentType = new ComponentType<>( Component1.class, createFn );

    assertEquals( componentType.getIndex(), 0 );
    componentType.initIndex( 23 );
    assertEquals( componentType.getIndex(), 23 );

    assertEquals( componentType.getType(), Component1.class );
    assertEquals( componentType.getCreateFn(), createFn );
    assertEquals( componentType.getName(), "Component1" );
    assertEquals( componentType.toString(), "ComponentType[Component1=23]" );
    assertEquals( componentType.hashCode(), 23 );
  }

  @Test
  public void equals()
  {
    final ComponentType<Component1> componentType1 = new ComponentType<>( Component1.class, Component1::new );
    componentType1.initIndex( 1 );
    final ComponentType<Component2> componentType2 = new ComponentType<>( Component2.class, Component2::new );
    componentType2.initIndex( 2 );
    assertEquals( componentType1, componentType1 );
    assertNotEquals( componentType2, componentType1 );
    assertEquals( componentType2, componentType2 );
  }

  @Test
  public void getName()
  {
    final ComponentType<Component1> componentType = new ComponentType<>( Component1.class, Component1::new );
    assertEquals( componentType.getName(), "Component1" );

    GaldrTestUtil.disableNames();

    assertInvariantFailure( componentType::getName,
                            "Galdr-0053: ComponentType.getName() invoked when Galdr.areNamesEnabled() returns false" );
  }

  @Test
  public void debugToString()
  {
    final ComponentType<Component1> componentType = new ComponentType<>( Component1.class, Component1::new );
    componentType.initIndex( 42 );

    assertEquals( componentType.toString(), "ComponentType[Component1=42]" );

    GaldrTestUtil.disableDebugToString();

    assertEquals( componentType.toString(), "galdr.ComponentType@2a" );
  }
}
