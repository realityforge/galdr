package galdr;

import java.util.BitSet;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComponentIdSetTest
  extends AbstractTest
{
  private static class Component1
  {
  }

  private static class Component2
  {
  }

  @Test
  public void basicOperation()
  {
    final World world = Worlds.world().component( Component1.class ).component( Component2.class ).build();

    final ComponentIdSet componentIdSet0 = world.createComponentIdSet();
    final ComponentIdSet componentIdSet0b = world.createComponentIdSet();
    final ComponentIdSet componentIdSet1 = world.createComponentIdSet( Component1.class );
    final ComponentIdSet componentIdSet1b = world.createComponentIdSet( Component1.class );
    final ComponentIdSet componentIdSet2 = world.createComponentIdSet( Component2.class );
    final ComponentIdSet componentIdSet2b = world.createComponentIdSet( Component2.class );
    final ComponentIdSet componentIdSet3 = world.createComponentIdSet( Component1.class, Component2.class );
    final ComponentIdSet componentIdSet3b = world.createComponentIdSet( Component1.class, Component2.class );

    final BitSet componentIds0 = set();
    final BitSet componentIds1 = set( 0 );
    final BitSet componentIds2 = set( 1 );
    final BitSet componentIds3 = set( 0, 1 );

    assertEquals( componentIdSet0.getBitSet(), componentIds0 );
    assertEquals( componentIdSet1.getBitSet(), componentIds1 );
    assertEquals( componentIdSet2.getBitSet(), componentIds2 );
    assertEquals( componentIdSet3.getBitSet(), componentIds3 );

    assertEquals( componentIdSet0, componentIdSet0 );
    assertEquals( componentIdSet0, componentIdSet0b );
    assertNotEquals( componentIdSet0, componentIdSet1 );
    assertNotEquals( componentIdSet0, componentIdSet2 );
    assertNotEquals( componentIdSet0, componentIdSet3 );

    assertNotEquals( componentIdSet1, componentIdSet0 );
    assertEquals( componentIdSet1, componentIdSet1 );
    assertEquals( componentIdSet1, componentIdSet1b );
    assertNotEquals( componentIdSet1, componentIdSet2 );
    assertNotEquals( componentIdSet1, componentIdSet3 );

    assertNotEquals( componentIdSet2, componentIdSet0 );
    assertNotEquals( componentIdSet2, componentIdSet1 );
    assertEquals( componentIdSet2, componentIdSet2 );
    assertEquals( componentIdSet2, componentIdSet2b );
    assertNotEquals( componentIdSet2, componentIdSet3 );

    assertNotEquals( componentIdSet3, componentIdSet0 );
    assertNotEquals( componentIdSet3, componentIdSet1 );
    assertNotEquals( componentIdSet3, componentIdSet2 );
    assertEquals( componentIdSet3, componentIdSet3 );
    assertEquals( componentIdSet3, componentIdSet3b );

    assertEquals( componentIdSet0.hashCode(), componentIdSet0b.hashCode() );
    assertNotEquals( componentIdSet0.hashCode(), componentIdSet1.hashCode() );
    assertNotEquals( componentIdSet0.hashCode(), componentIdSet2.hashCode() );
    assertNotEquals( componentIdSet0.hashCode(), componentIdSet3.hashCode() );

    assertNotEquals( componentIdSet1.hashCode(), componentIdSet0.hashCode() );
    assertEquals( componentIdSet1.hashCode(), componentIdSet1b.hashCode() );
    assertNotEquals( componentIdSet1.hashCode(), componentIdSet2.hashCode() );
    assertNotEquals( componentIdSet1.hashCode(), componentIdSet3.hashCode() );

    assertNotEquals( componentIdSet2.hashCode(), componentIdSet0.hashCode() );
    assertNotEquals( componentIdSet2.hashCode(), componentIdSet1.hashCode() );
    assertEquals( componentIdSet2.hashCode(), componentIdSet2b.hashCode() );
    assertNotEquals( componentIdSet2.hashCode(), componentIdSet3.hashCode() );

    assertNotEquals( componentIdSet3.hashCode(), componentIdSet0.hashCode() );
    assertNotEquals( componentIdSet3.hashCode(), componentIdSet1.hashCode() );
    assertNotEquals( componentIdSet3.hashCode(), componentIdSet2.hashCode() );
    assertEquals( componentIdSet3.hashCode(), componentIdSet3b.hashCode() );
  }

  @Test
  public void toString_test()
  {
    final World world = Worlds.world().component( Component1.class ).component( Component2.class ).build();

    final ComponentIdSet componentIdSet0 = world.createComponentIdSet();
    final ComponentIdSet componentIdSet1 = world.createComponentIdSet( Component1.class );
    final ComponentIdSet componentIdSet2 = world.createComponentIdSet( Component2.class );
    final ComponentIdSet componentIdSet3 = world.createComponentIdSet( Component1.class, Component2.class );

    assertEquals( componentIdSet0.toString(), "ComponentIdSet[]" );
    assertEquals( componentIdSet1.toString(), "ComponentIdSet[0]" );
    assertEquals( componentIdSet2.toString(), "ComponentIdSet[1]" );
    assertEquals( componentIdSet3.toString(), "ComponentIdSet[0, 1]" );

    GaldrTestUtil.disableDebugToString();

    assertDefaultToString( componentIdSet0 );
    assertDefaultToString( componentIdSet1 );
    assertDefaultToString( componentIdSet2 );
    assertDefaultToString( componentIdSet3 );
  }

  @Test
  public void duplicateComponentIdWhenConstructing()
  {
    final World world = Worlds.world().component( Component1.class ).component( Component2.class ).build();

    assertInvariantFailure( () -> world.createComponentIdSet( Component1.class, Component2.class, Component1.class ),
                            "Galdr-0049: A duplicate component named 'galdr.ComponentIdSetTest$Component1' was passed when attempting to create ComponentIdSet." );
  }
}
