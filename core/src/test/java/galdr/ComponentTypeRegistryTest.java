package galdr;

import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComponentTypeRegistryTest
  extends AbstractTest
{
  private static class Component1
  {
  }

  private static class Component2
  {
  }

  private static class Component3
  {
  }

  @Test
  public void basicOperation()
  {
    final ComponentTypeRegistry registry =
      new ComponentTypeRegistry( new FastArrayComponentManager<>( Component1.class, Component1::new ),
                                 new FastArrayComponentManager<>( Component2.class, Component2::new ),
                                 new FastArrayComponentManager<>( Component3.class, Component3::new ) );
    assertEquals( registry.size(), 3 );
    assertTypeRegistered( registry, Component1.class, 0 );
    assertTypeRegistered( registry, Component2.class, 1 );
    assertTypeRegistered( registry, Component3.class, 2 );
  }

  private void assertTypeRegistered( @Nonnull final ComponentTypeRegistry registry,
                                     @Nonnull final Class<?> type,
                                     final int index )
  {
    final ComponentManager entry = registry.getComponentManagerByIndex( index );
    assertEquals( entry.getIndex(), index );
    assertEquals( entry.getType(), type );
    assertEquals( registry.getComponentManagerByType( type ), entry );
  }
}
