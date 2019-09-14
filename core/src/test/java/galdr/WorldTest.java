package galdr;

import java.util.Set;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class WorldTest
  extends AbstractTest
{
  private static class Component1
  {
  }

  private static class Component2
  {
  }

  @Test
  public void getComponentByType()
  {
    final ComponentManager<Component1> componentManager =
      new FastArrayComponentManager<>( 0, Component1.class, Component1::new );
    final World world = new World( componentManager );

    final ComponentAPI<Component1> api = world.getComponentByType( Component1.class );
    assertNotNull( api );
    assertEquals( api, componentManager.getApi() );
  }

  @Test
  public void getComponentTypes()
  {
    final ComponentManager<Component1> componentManager1 =
      new FastArrayComponentManager<>( 0, Component1.class, Component1::new );
    final ComponentManager<Component2> componentManager2 =
      new FastArrayComponentManager<>( 1, Component2.class, Component2::new );
    final World world = new World( componentManager1, componentManager2 );

    final Set<Class<?>> componentTypes = world.getComponentTypes();
    assertEquals( componentTypes.size(), 2 );
    assertTrue( componentTypes.contains( Component1.class ) );
    assertTrue( componentTypes.contains( Component2.class ) );
  }
}
