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
      new ComponentTypeRegistry( new FastArrayComponentStore<>( Component1.class, Component1::new ),
                                 new FastArrayComponentStore<>( Component2.class, Component2::new ),
                                 new FastArrayComponentStore<>( Component3.class, Component3::new ) );
    assertEquals( registry.size(), 3 );
    assertTypeRegistered( registry, Component1.class, 0 );
    assertTypeRegistered( registry, Component2.class, 1 );
    assertTypeRegistered( registry, Component3.class, 2 );
  }

  private void assertTypeRegistered( @Nonnull final ComponentTypeRegistry registry,
                                     @Nonnull final Class<?> type,
                                     final int index )
  {
    final ComponentStore entry = registry.getComponentStoreByIndex( index );
    assertEquals( entry.getIndex(), index );
    assertEquals( entry.getType(), type );
    assertEquals( registry.getComponentStoreByType( type ), entry );
  }
}
