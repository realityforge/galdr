package galdr;

import galdr.spy.CollectionInfo;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class CollectionInfoImplTest
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
    final World world = Worlds.world().component( Component1.class ).build();
    final Subscription subscription1 = createSubscription( world, Collections.singletonList( Component1.class ) );
    final Collection<CollectionInfo> collections = world.getSpy().getCollections().values();
    assertEquals( collections.size(), 1 );
    final CollectionInfo info = collections.iterator().next();
    assertEquals( info.getAreaOfInterest(),
                  world.createAreaOfInterest( Collections.singletonList( Component1.class ) ) );
    assertEquals( info.getSubscriptionCount(), 1 );
    assertFalse( info.isDisposed() );

    final Subscription subscription2 = createSubscription( world, Collections.singletonList( Component1.class ) );

    assertEquals( info.getSubscriptionCount(), 2 );
    assertFalse( info.isDisposed() );

    run( world, subscription2::dispose );

    assertEquals( info.getSubscriptionCount(), 1 );
    assertFalse( info.isDisposed() );

    assertEquals( info.getEntityCount(), 0 );
    final int entityId1 = createEntity( world, Component1.class );
    assertEquals( info.getEntityCount(), 1 );
    final int entityId2 = createEntity( world, Component1.class );
    final int entityId3 = createEntity( world, Component1.class );
    assertEquals( info.getEntityCount(), 3 );

    run( world, () -> world.disposeEntity( entityId1 ) );

    assertEquals( info.getEntityCount(), 2 );
    run( world, () -> world.disposeEntity( entityId2 ) );

    assertEquals( info.getEntityCount(), 1 );

    run( world, subscription1::dispose );

    assertEquals( info.getSubscriptionCount(), 0 );
    assertTrue( info.isDisposed() );
  }

  @Test
  public void equalsAndHashCode()
  {
    final World world = Worlds.world().component( Component1.class ).component( Component2.class ).build();
    final AreaOfInterest areaOfInterest1 = world.createAreaOfInterest( Collections.singletonList( Component1.class ) );
    final AreaOfInterest areaOfInterest2 = world.createAreaOfInterest( Collections.singletonList( Component2.class ) );
    createSubscription( world, Collections.singletonList( Component1.class ) );
    createSubscription( world, Collections.singletonList( Component2.class ) );

    final Map<AreaOfInterest, CollectionInfo> collections = world.getSpy().getCollections();
    final CollectionInfo info1 = collections.get( areaOfInterest1 );
    assertNotNull( info1 );
    final CollectionInfo info1b = new CollectionInfoImpl( world.getEntityCollections().get( areaOfInterest1 ) );
    final CollectionInfo info2 = collections.get( areaOfInterest2 );
    assertNotNull( info2 );

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
    final Subscription subscription = createSubscription( world, Collections.singletonList( Component1.class ) );
    final EntityCollection collection = subscription.getCollection();

    assertInvariantFailure( collection::asInfo,
                            "Galdr-0040: EntityCollection.asInfo() invoked but Galdr.areSpiesEnabled() returned false." );
  }
}
