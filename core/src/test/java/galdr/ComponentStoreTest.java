package galdr;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComponentStoreTest
  extends AbstractTest
{
  private static class Component1
  {
    int value;
  }

  @Test
  public void basicOperation()
  {
    final ComponentType componentType = new ComponentType( Component1.class, Component1::new );

    final ComponentStore componentStore = new FastArrayComponentStore( componentType );

    assertEquals( componentStore.getComponentType(), componentType );
    assertEquals( componentStore.getName(), componentType.getName() );

    final int entityId = randomInt( 42 );
    assertFalse( componentStore.has( entityId ) );
    assertNull( componentStore.find( entityId ) );
    assertInvariantFailure( () -> componentStore.get( entityId ),
                            "Galdr-0033: The ComponentStore.get() method for the component named " +
                            "'Component1' expected to find a component for entityId " + entityId +
                            " but is unable to locate component." );

    final Component1 component = (Component1) componentStore.findOrCreate( entityId );
    assertNotNull( component );

    final int initialValue = randomInt( 456738 );
    component.value = initialValue;

    assertInvariantFailure( () -> componentStore.create( entityId ),
                            "Galdr-0031: The ComponentStore.create() method invoked but entity " +
                            entityId + " already has the component named 'Component1'." );

    assertTrue( componentStore.has( entityId ) );
    assertEquals( componentStore.find( entityId ), component );
    assertEquals( componentStore.get( entityId ), component );
    assertEquals( ( (Component1) componentStore.get( entityId ) ).value, initialValue );

    componentStore.remove( entityId );

    assertFalse( componentStore.has( entityId ) );
    assertNull( componentStore.find( entityId ) );

    assertInvariantFailure( () -> componentStore.remove( entityId ),
                            "Galdr-0030: The ComponentStore.remove() method for the component named " +
                            "'Component1' was invoked but the entity " + entityId + " does not have the component." );
  }

  @Test
  public void debugToString()
  {
    final ComponentType componentType = new ComponentType( Component1.class, Component1::new );

    final ComponentStore componentStore = new FastArrayComponentStore( componentType );

    assertEquals( componentStore.toString(), "ComponentStore[Component1]" );

    GaldrTestUtil.disableDebugToString();

    assertNotEquals( componentStore.toString(), "ComponentStore[Component1]" );
  }

  @Test
  public void errorOnNegativeEntityId()
  {
    final ComponentStore componentStore =
      new FastArrayComponentStore( new ComponentType( Component1.class, Component1::new ) );

    assertInvariantFailure( () -> componentStore.has( -23 ),
                            "Galdr-0029: The ComponentStore method invoked was with a negative entityId -23." );
    assertInvariantFailure( () -> componentStore.find( -23 ),
                            "Galdr-0029: The ComponentStore method invoked was with a negative entityId -23." );
    assertInvariantFailure( () -> componentStore.get( -23 ),
                            "Galdr-0029: The ComponentStore method invoked was with a negative entityId -23." );
    assertInvariantFailure( () -> componentStore.create( -23 ),
                            "Galdr-0029: The ComponentStore method invoked was with a negative entityId -23." );
    assertInvariantFailure( () -> componentStore.remove( -23 ),
                            "Galdr-0029: The ComponentStore method invoked was with a negative entityId -23." );
  }
}
