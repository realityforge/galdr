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
    final World world = Galdr.world().component( Component1.class, Component1::new ).build();

    assertNotNull( world.getComponentByType( Component1.class ) );
  }

  @Test
  public void getComponentTypes()
  {
    final World world = Galdr.world()
      .component( Component1.class, Component1::new )
      .component( Component2.class, Component2::new )
      .build();

    final Set<Class<?>> componentTypes = world.getComponentRegistry().getComponentTypes();
    assertEquals( componentTypes.size(), 2 );
    assertTrue( componentTypes.contains( Component1.class ) );
    assertTrue( componentTypes.contains( Component2.class ) );
  }
}
