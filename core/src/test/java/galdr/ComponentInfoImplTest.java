package galdr;

import galdr.spy.ComponentInfo;
import java.util.Collections;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComponentInfoImplTest
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
    final ComponentManager<Component1> componentManager = world.getComponentManagerByType( Component1.class );
    final ComponentInfo info = world.getSpy().getComponentByType( Component1.class );

    assertEquals( info.getId(), componentManager.getId() );
    assertEquals( info.getName(), componentManager.getName() );
    assertEquals( info.getCollectionCount(), 0 );
    assertEquals( info.toString(), componentManager.toString() );

    // This verifies that it is exactly the same instance
    //noinspection SimplifiedTestNGAssertion
    assertTrue( info == world.getSpy().getComponentByType( Component1.class ) );
    //noinspection SimplifiedTestNGAssertion
    assertTrue( info == componentManager.asInfo() );
  }

  @Test
  public void getCollectionCount()
  {
    final World world = Worlds.world().component( Component1.class ).component( Component2.class ).build();
    final ComponentInfo info = world.getSpy().getComponentByType( Component1.class );

    assertEquals( info.getCollectionCount(), 0 );

    final AreaOfInterest areaOfInterest1 =
      world.createAreaOfInterest( Collections.singletonList( Component1.class ),
                                  Collections.emptyList(),
                                  Collections.emptyList() );
    final AreaOfInterest areaOfInterest2 =
      world.createAreaOfInterest( Collections.emptyList(),
                                  Collections.singletonList( Component1.class ),
                                  Collections.emptyList() );
    final AreaOfInterest areaOfInterest3 =
      world.createAreaOfInterest( Collections.emptyList(),
                                  Collections.emptyList(),
                                  Collections.singletonList( Component1.class ) );
    final AreaOfInterest areaOfInterest4 =
      world.createAreaOfInterest( Collections.singletonList( Component2.class ),
                                  Collections.emptyList(),
                                  Collections.emptyList() );

    assertEquals( info.getCollectionCount(), 0 );

    final Subscription subscription1 = run( world, () -> world.createSubscription( areaOfInterest1 ) );

    assertEquals( info.getCollectionCount(), 1 );

    run( world, () -> world.createSubscription( areaOfInterest2 ) );

    assertEquals( info.getCollectionCount(), 2 );

    run( world, () -> world.createSubscription( areaOfInterest3 ) );

    assertEquals( info.getCollectionCount(), 3 );

    final Subscription subscription4 = run( world, () -> world.createSubscription( areaOfInterest4 ) );

    assertEquals( info.getCollectionCount(), 3 );

    run( world, subscription4::dispose );

    assertEquals( info.getCollectionCount(), 3 );

    run( world, subscription1::dispose );

    assertEquals( info.getCollectionCount(), 2 );
  }

  @Test
  public void equalsAndHashCode()
  {
    final World world = Worlds.world().component( Component1.class ).component( Component2.class ).build();
    final ComponentInfo info1 = world.getSpy().getComponentByType( Component1.class );
    final ComponentInfoImpl info1b = new ComponentInfoImpl( world.getComponentManagerByType( Component1.class ) );
    final ComponentInfo info2 = world.getSpy().getComponentByType( Component2.class );

    assertEquals( info1, info1b );
    assertNotEquals( info1, info2 );
    assertEquals( info1b, info1 );
    assertNotEquals( info1b, info2 );
    assertNotEquals( info2, info1 );
    assertNotEquals( info2, info1b );
    assertNotEquals( info1, "X" );

    assertEquals( info1.hashCode(), info1b.hashCode() );
    assertNotEquals( info1.hashCode(), info2.hashCode() );
    assertEquals( info1b.hashCode(), info1.hashCode() );
    assertNotEquals( info1b.hashCode(), info2.hashCode() );
    assertNotEquals( info2.hashCode(), info1.hashCode() );
    assertNotEquals( info2.hashCode(), info1b.hashCode() );

    //noinspection SimplifiedTestNGAssertion,EqualsBetweenInconvertibleTypes
    assertFalse( info2.equals( randomString() ) );
  }

  @Test
  public void spyDisabled()
  {
    GaldrTestUtil.disableSpies();

    final World world = Worlds.world().component( Component1.class ).build();
    final ComponentManager<Component1> componentManager = world.getComponentManagerByType( Component1.class );

    assertInvariantFailure( componentManager::asInfo,
                            "Galdr-0040: ComponentManager.asInfo() invoked but Galdr.areSpiesEnabled() returned false." );
  }
}
