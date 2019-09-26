package galdr;

import galdr.spy.ComponentAddCompleteEvent;
import galdr.spy.ComponentAddStartEvent;
import galdr.spy.ComponentRemoveStartEvent;
import java.util.BitSet;
import java.util.function.Supplier;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComponentManagerTest
  extends AbstractTest
{
  private static class Component1
  {
    int value;
  }

  @Test
  public void basicOperation()
  {
    final Supplier<Component1> createFn = Component1::new;
    final World world = Worlds.world().component( Component1.class, createFn ).build();
    final ComponentManager<Component1> componentManager =
      world.getComponentRegistry().getComponentManagerByType( Component1.class );

    assertEquals( componentManager.getWorld(), world );
    assertEquals( componentManager.getId(), 0 );
    assertNotNull( componentManager.getApi() );
    assertEquals( componentManager.getType(), Component1.class );
    assertEquals( componentManager.getCreateFn(), createFn );
    assertEquals( componentManager.getName(), "Component1" );
    assertEquals( componentManager.toString(), "ComponentManager[Component1=0]" );
    assertEquals( componentManager.hashCode(), 0 );

    final int entityId = world.createEntity( new BitSet() );
    assertFalse( componentManager.has( entityId ) );
    assertNull( componentManager.find( entityId ) );
    assertInvariantFailure( () -> componentManager.get( entityId ),
                            "Galdr-0033: The ComponentManager.get() method for the component named " +
                            "'Component1' expected to find a component for entityId " + entityId +
                            " but is unable to locate component." );

    final Component1 component = componentManager.findOrCreate( entityId );
    assertNotNull( component );

    final int initialValue = randomInt( 456738 );
    component.value = initialValue;

    assertInvariantFailure( () -> componentManager.create( entityId ),
                            "Galdr-0031: The ComponentManager.create() method invoked but entity " +
                            entityId + " already has the component named 'Component1'." );

    assertTrue( componentManager.has( entityId ) );
    assertEquals( componentManager.find( entityId ), component );
    assertEquals( componentManager.get( entityId ), component );
    assertEquals( componentManager.get( entityId ).value, initialValue );

    componentManager.remove( entityId );

    assertFalse( componentManager.has( entityId ) );
    assertNull( componentManager.find( entityId ) );

    assertInvariantFailure( () -> componentManager.remove( entityId ),
                            "Galdr-0030: The ComponentManager.remove() method for the component named " +
                            "'Component1' was invoked but the entity " + entityId + " does not have the component." );
  }

  @Test
  public void basicComponentLifecycle_spyEnabled()
  {
    final World world = Worlds.world().component( Component1.class, Component1::new ).build();
    final ComponentManager<Component1> componentManager =
      world.getComponentRegistry().getComponentManagerByType( Component1.class );

    final int entityId = world.createEntity( new BitSet() );

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( world );

    final Component1 component = componentManager.findOrCreate( entityId );
    assertNotNull( component );

    final int initialValue = randomInt( 44 );
    component.value = initialValue;

    assertTrue( componentManager.has( entityId ) );
    assertEquals( componentManager.find( entityId ), component );
    assertEquals( componentManager.get( entityId ), component );
    assertEquals( componentManager.get( entityId ).value, initialValue );

    componentManager.remove( entityId );

    assertFalse( componentManager.has( entityId ) );
    assertNull( componentManager.find( entityId ) );

    handler.assertEventCount( 3 );
    handler.assertNextEvent( ComponentAddStartEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), entityId );
      assertEquals( e.getComponentId(), componentManager.getId() );
    } );
    handler.assertNextEvent( ComponentAddCompleteEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), entityId );
      assertEquals( e.getComponentId(), componentManager.getId() );
    } );
    handler.assertNextEvent( ComponentRemoveStartEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), entityId );
      assertEquals( e.getComponentId(), componentManager.getId() );
    } );
  }

  @Test
  public void debugToString()
  {
    final World world = Worlds.world().component( Component1.class, Component1::new ).build();
    final ComponentManager<Component1> componentManager =
      world.getComponentRegistry().getComponentManagerByType( Component1.class );

    assertEquals( componentManager.toString(), "ComponentManager[Component1=0]" );

    GaldrTestUtil.disableDebugToString();

    assertDefaultToString( componentManager );
  }

  @Test
  public void getName()
  {
    final World world = Worlds.world().component( Component1.class, Component1::new ).build();
    final ComponentManager<Component1> componentManager =
      world.getComponentRegistry().getComponentManagerByType( Component1.class );

    assertEquals( componentManager.getName(), "Component1" );

    GaldrTestUtil.disableNames();

    assertInvariantFailure( componentManager::getName,
                            "Galdr-0053: ComponentManager.getName() invoked when Galdr.areNamesEnabled() returns false" );
  }

  @Test
  public void errorOnUnAllocatedEntity()
  {
    final World world = Worlds.world().component( Component1.class, Component1::new ).build();
    final ComponentManager<Component1> componentManager =
      world.getComponentRegistry().getComponentManagerByType( Component1.class );

    assertInvariantFailure( () -> componentManager.has( -23 ),
                            "Galdr-0079: Attempting to get entity -23 but entity is not allocated." );
    assertInvariantFailure( () -> componentManager.find( -23 ),
                            "Galdr-0079: Attempting to get entity -23 but entity is not allocated." );
    assertInvariantFailure( () -> componentManager.get( -23 ),
                            "Galdr-0079: Attempting to get entity -23 but entity is not allocated." );
    assertInvariantFailure( () -> componentManager.create( -23 ),
                            "Galdr-0079: Attempting to get entity -23 but entity is not allocated." );
    assertInvariantFailure( () -> componentManager.remove( -23 ),
                            "Galdr-0079: Attempting to get entity -23 but entity is not allocated." );
  }
}
