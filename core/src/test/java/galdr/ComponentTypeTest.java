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

  @Test
  public void basicOperation()
  {
    final ComponentType componentType = new ComponentType( Component1.class, 23 );
    assertEquals( componentType.getIndex(), 23 );
    assertEquals( componentType.getType(), Component1.class );
    assertEquals( componentType.getName(), "Component1" );
    assertEquals( componentType.toString(), "ComponentType[Component1=23]" );
  }
}
