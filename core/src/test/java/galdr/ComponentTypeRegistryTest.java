package galdr;

import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComponentTypeRegistryTest
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

  private static class Component3
    implements Component
  {
  }

  @Test
  public void basicOperation()
  {
    final ComponentTypeRegistry registry =
      new ComponentTypeRegistry( Component1.class, Component2.class, Component3.class );
    assertEquals( registry.size(), 3 );
    assertTypeRegistered( registry, Component1.class, 0 );
    assertTypeRegistered( registry, Component2.class, 1 );
    assertTypeRegistered( registry, Component3.class, 2 );
  }

  private void assertTypeRegistered( @Nonnull final ComponentTypeRegistry registry,
                                     @Nonnull final Class<? extends Component> type,
                                     final int index )
  {
    final ComponentType entry = registry.getComponentTypeByIndex( index );
    assertEquals( entry.getIndex(), index );
    assertEquals( entry.getType(), type );
    assertEquals( registry.getComponentTypeByType( type ), entry );
  }
}
