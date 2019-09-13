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
    final ComponentType componentType = new ComponentType( Component1.class, 23, createFn );
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
    final ComponentType componentType1 = new ComponentType( Component1.class, 1, Component1::new );
    final ComponentType componentType2 = new ComponentType( Component2.class, 2, Component2::new );
    assertEquals( componentType1, componentType1 );
    assertNotEquals( componentType2, componentType1 );
    assertEquals( componentType2, componentType2 );
  }

  @Test
  public void getName()
  {
    final ComponentType componentType = new ComponentType( Component1.class, 1, Component1::new );
    assertEquals( componentType.getName(), "Component1" );

    GaldrTestUtil.disableNames();

    assertInvariantFailure( componentType::getName,
                            "Galdr-0053: ComponentType.getName() invoked when Galdr.areNamesEnabled() returns false" );
  }
}
