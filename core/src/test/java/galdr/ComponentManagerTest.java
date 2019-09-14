package galdr;

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
    final ComponentManager<Component1> componentManager = new FastArrayComponentManager<>( Component1.class, createFn );

    // This is verifying that index is invalid sentinel value
    assertEquals( componentManager.toString(), "ComponentManager[Component1=-1]" );
    componentManager.initIndex( 23 );
    assertEquals( componentManager.getIndex(), 23 );

    assertEquals( componentManager.getType(), Component1.class );
    assertEquals( componentManager.getCreateFn(), createFn );
    assertEquals( componentManager.getName(), "Component1" );
    assertEquals( componentManager.toString(), "ComponentManager[Component1=23]" );
    assertEquals( componentManager.hashCode(), 23 );

    final int entityId = randomInt( 42 );
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
  public void debugToString()
  {
    final ComponentManager<Component1> componentManager =
      new FastArrayComponentManager<>( Component1.class, Component1::new );
    componentManager.initIndex( 42 );

    assertEquals( componentManager.toString(), "ComponentManager[Component1=42]" );

    GaldrTestUtil.disableDebugToString();

    assertNotEquals( componentManager.toString(), "ComponentManager[Component1=42]" );
  }

  @Test
  public void verifyIndexInitializationWillInvariantIfSetTwice()
  {
    final Supplier<Component1> createFn = Component1::new;
    final ComponentManager<Component1> componentManager = new FastArrayComponentManager<>( Component1.class, createFn );

    assertInvariantFailure( componentManager::getIndex,
                            "Galdr-0021: ComponentManager.getIndex() invoked before index initialized" );
    componentManager.initIndex( 23 );
    assertEquals( componentManager.getIndex(), 23 );

    assertInvariantFailure( () -> componentManager.initIndex( 77 ),
                            "Galdr-0020: ComponentManager.initIndex() invoked but index is already initialized" );
  }

  @Test
  public void errorOnNegativeEntityId()
  {
    final ComponentManager<Component1> componentManager =
      new FastArrayComponentManager<>( Component1.class, Component1::new );

    assertInvariantFailure( () -> componentManager.has( -23 ),
                            "Galdr-0029: The ComponentManager method invoked was with a negative entityId -23." );
    assertInvariantFailure( () -> componentManager.find( -23 ),
                            "Galdr-0029: The ComponentManager method invoked was with a negative entityId -23." );
    assertInvariantFailure( () -> componentManager.get( -23 ),
                            "Galdr-0029: The ComponentManager method invoked was with a negative entityId -23." );
    assertInvariantFailure( () -> componentManager.create( -23 ),
                            "Galdr-0029: The ComponentManager method invoked was with a negative entityId -23." );
    assertInvariantFailure( () -> componentManager.remove( -23 ),
                            "Galdr-0029: The ComponentManager method invoked was with a negative entityId -23." );
  }
}
