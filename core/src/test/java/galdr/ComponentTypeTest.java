package galdr;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComponentTypeTest
  extends AbstractTest
{
  private static class Component1
    implements Component
  {
  }

  private static class Component2
    implements Component
  {
  }

  @Test
  public void basicOperation()
  {
    final ComponentType componentType = new ComponentType( Component1.class, 23 );
    assertEquals( componentType.getIndex(), 23 );
    assertEquals( componentType.getType(), Component1.class );
    assertEquals( componentType.getName(), "Component1" );
    assertEquals( componentType.toString(), "ComponentType[Component1=23]" );
    assertEquals( componentType.hashCode(), 23 );
  }

  @Test
  public void equals()
  {
    final ComponentType componentType1 = new ComponentType( Component1.class, 1 );
    final ComponentType componentType2 = new ComponentType( Component2.class, 2 );
    assertEquals( componentType1, componentType1 );
    assertNotEquals( componentType2, componentType1 );
    assertEquals( componentType2, componentType2 );
  }

  @Test
  public void getName()
  {
    final ComponentType componentType = new ComponentType( Component1.class, 1 );
    assertEquals( componentType.getName(), "Component1" );

    GaldrTestUtil.disableNames();

    assertInvariantFailure( componentType::getName,
                            "Galdr-0053: ComponentType.getName() invoked when Galdr.areNamesEnabled() returns false" );
  }
}
