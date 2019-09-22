package galdr;

import java.util.Arrays;
import java.util.HashSet;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComponentRegistryTest
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
    final World world = Galdr.world().build();
    final ComponentRegistry registry =
      new ComponentRegistry( new FastArrayComponentManager<>( world, 0, Component1.class, Component1::new, 120 ),
                             new FastArrayComponentManager<>( world, 1, Component2.class, Component2::new, 120 ),
                             new FastArrayComponentManager<>( world, 2, Component3.class, Component3::new, 120 ) );
    assertEquals( registry.size(), 3 );
    assertTypeRegistered( registry, Component1.class, 0 );
    assertTypeRegistered( registry, Component2.class, 1 );
    assertTypeRegistered( registry, Component3.class, 2 );

    assertEquals( registry.getComponentTypes(),
                  new HashSet<>( Arrays.asList( Component1.class, Component2.class, Component3.class ) ) );
  }

  @Test
  public void disableCopyArrayDuringConstruct()
  {
    GaldrTestUtil.disableCopyArraysPassedToConstructors();
    final World world = Galdr.world().build();
    final ComponentRegistry registry =
      new ComponentRegistry( new FastArrayComponentManager<>( world, 0, Component1.class, Component1::new, 120 ),
                             new FastArrayComponentManager<>( world, 1, Component2.class, Component2::new, 120 ),
                             new FastArrayComponentManager<>( world, 2, Component3.class, Component3::new, 120 ) );
    assertEquals( registry.size(), 3 );
    assertTypeRegistered( registry, Component1.class, 0 );
    assertTypeRegistered( registry, Component2.class, 1 );
    assertTypeRegistered( registry, Component3.class, 2 );
  }

  private void assertTypeRegistered( @Nonnull final ComponentRegistry registry,
                                     @Nonnull final Class<?> type,
                                     final int id )
  {
    final ComponentManager<?> entry = registry.getComponentManagerByIndex( id );
    assertEquals( entry.getId(), id );
    assertEquals( entry.getType(), type );
    assertEquals( registry.getComponentManagerByType( type ), entry );
  }

  @Test
  public void constructContainingComponentsWithInvalidIndex()
  {
    final World world = Galdr.world().build();
    assertInvariantFailure( () -> new ComponentRegistry( new FastArrayComponentManager<>( world,
                                                                                          0,
                                                                                          Component1.class,
                                                                                          Component1::new, 120 ),
                                                         new FastArrayComponentManager<>( world,
                                                                                          1,
                                                                                          Component2.class,
                                                                                          Component2::new, 120 ),
                                                         new FastArrayComponentManager<>( world,
                                                                                          3,
                                                                                          Component3.class,
                                                                                          Component3::new, 120 ) ),
                            "Galdr-0003: Component named 'Component3' has id 3 but was passed as index 2." );
  }

  @Test
  public void getComponentManagerByIndex_badIndex()
  {
    final World world = Galdr.world().build();
    final ComponentRegistry registry =
      new ComponentRegistry( new FastArrayComponentManager<>( world, 0, Component1.class, Component1::new, 120 ) );
    assertInvariantFailure( () -> registry.getComponentManagerByIndex( -1 ),
                            "Galdr-0002: ComponentRegistry.getComponentManagerByIndex() attempted to access Component at index -1 but no such component exists." );
    assertInvariantFailure( () -> registry.getComponentManagerByIndex( 1 ),
                            "Galdr-0002: ComponentRegistry.getComponentManagerByIndex() attempted to access Component at index 1 but no such component exists." );
  }

  @Test
  public void getComponentManagerByType_badType()
  {
    final World world = Galdr.world().build();
    final ComponentRegistry registry =
      new ComponentRegistry( new FastArrayComponentManager<>( world, 0, Component1.class, Component1::new, 120 ) );
    assertInvariantFailure( () -> registry.getComponentManagerByType( Component2.class ),
                            "Galdr-0001: ComponentRegistry.getComponentManagerByType() attempted to access Component for type class galdr.ComponentRegistryTest$Component2 but no such component exists." );
  }
}
