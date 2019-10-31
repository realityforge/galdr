package galdr;

import galdr.spy.CollectionAttachEvent;
import galdr.spy.CollectionCreateCompleteEvent;
import galdr.spy.CollectionCreateStartEvent;
import galdr.spy.CollectionDetachEvent;
import galdr.spy.CollectionDisposeCompleteEvent;
import galdr.spy.CollectionDisposeStartEvent;
import galdr.spy.SubscriptionCreateCompleteEvent;
import galdr.spy.SubscriptionCreateStartEvent;
import galdr.spy.SubscriptionDisposeCompleteEvent;
import galdr.spy.SubscriptionDisposeStartEvent;
import java.util.Collections;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class SubscriptionTest
  extends AbstractTest
{
  private static class Component1
  {
  }

  @Test
  public void construct()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final AreaOfInterest areaOfInterest =
      world.createAreaOfInterest( Collections.singletonList( Component1.class ) );

    assertEquals( world.getSubscriptions().size(), 0 );

    final Subscription subscription = run( world, () -> world.createSubscription( areaOfInterest ) );

    assertEquals( subscription.getId(), 1 );
    assertEquals( subscription.getName(), "Subscription@1" );
    assertEquals( subscription.getCollection().getAreaOfInterest(), areaOfInterest );

    assertEquals( world.getSubscriptions().size(), 1 );
    assertEquals( world.getSubscriptions().get( 1 ), subscription );
  }

  @Test
  public void construct_spiesEnabled()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final AreaOfInterest areaOfInterest =
      world.createAreaOfInterest( Collections.singletonList( Component1.class ) );

    assertEquals( world.getSubscriptions().size(), 0 );

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( world );
    final Subscription subscription = run( world, () -> world.createSubscription( areaOfInterest ) );
    handler.unsubscribe();

    assertEquals( subscription.getId(), 1 );
    assertEquals( subscription.getName(), "Subscription@1" );
    assertEquals( subscription.getCollection().getAreaOfInterest(), areaOfInterest );

    assertEquals( world.getSubscriptions().size(), 1 );
    assertEquals( world.getSubscriptions().get( 1 ), subscription );

    handler.assertEventCount( 4 );
    handler.assertNextEvent( SubscriptionCreateStartEvent.class, e -> {
      assertEquals( e.getId(), subscription.getId() );
      assertEquals( e.getName(), subscription.getName() );
      assertEquals( e.getAreaOfInterest(), areaOfInterest );
    } );
    handler.assertNextEvent( CollectionCreateStartEvent.class,
                             e -> assertEquals( e.getCollection().getAreaOfInterest(), areaOfInterest ) );
    handler.assertNextEvent( CollectionCreateCompleteEvent.class, e -> {
      assertEquals( e.getCollection().getSubscriptionCount(), 1 );
      assertEquals( e.getCollection().getAreaOfInterest(), areaOfInterest );
    } );
    handler.assertNextEvent( SubscriptionCreateCompleteEvent.class, e -> {
      assertEquals( e.getSubscription().getId(), subscription.getId() );
      assertEquals( e.getSubscription().getName(), subscription.getName() );
      assertEquals( e.getSubscription().getCollection().getAreaOfInterest(), areaOfInterest );
    } );
  }

  @Test
  public void construct_spiesEnabled_whenAttachToExistingCollection()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final AreaOfInterest areaOfInterest =
      world.createAreaOfInterest( Collections.singletonList( Component1.class ) );

    run( world, () -> world.createSubscription( areaOfInterest ) );

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( world );
    final Subscription subscription = run( world, () -> world.createSubscription( areaOfInterest ) );
    handler.unsubscribe();

    assertEquals( subscription.getId(), 2 );
    assertEquals( subscription.getName(), "Subscription@2" );
    assertEquals( subscription.getCollection().getAreaOfInterest(), areaOfInterest );

    handler.assertEventCount( 3 );
    handler.assertNextEvent( SubscriptionCreateStartEvent.class, e -> {
      assertEquals( e.getId(), subscription.getId() );
      assertEquals( e.getName(), subscription.getName() );
      assertEquals( e.getAreaOfInterest(), areaOfInterest );
    } );
    handler.assertNextEvent( CollectionAttachEvent.class,
                             e -> assertEquals( e.getCollection().getAreaOfInterest(), areaOfInterest ) );
    handler.assertNextEvent( SubscriptionCreateCompleteEvent.class, e -> {
      assertEquals( e.getSubscription().getId(), subscription.getId() );
      assertEquals( e.getSubscription().getName(), subscription.getName() );
      assertEquals( e.getSubscription().getCollection().getAreaOfInterest(), areaOfInterest );
    } );
  }

  @Test
  public void construct_namedPassedButNamesDisabled()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final Subscription subscription = createSubscription( world, Collections.singletonList( Component1.class ) );
    GaldrTestUtil.disableNames();
    assertInvariantFailure( () -> new Subscription( world,
                                                    23,
                                                    "MyName",
                                                    subscription.getCollection().getAreaOfInterest() ),
                            "Galdr-0052: Subscription passed a name 'MyName' but Galdr.areNamesEnabled() is false" );
  }

  @Test
  public void getSubscriptions_spiesDisabled()
  {
    final World world = Worlds.world().build();
    GaldrTestUtil.disableSpies();
    assertInvariantFailure( world::getSubscriptions,
                            "Galdr-0050: World.getSubscriptions() invoked but Galdr.areSpiesEnabled() returns false." );
  }

  @Test
  public void getName()
  {
    final World world = Worlds.world().component( Component1.class ).build();

    final AreaOfInterest areaOfInterest =
      world.createAreaOfInterest( Collections.singletonList( Component1.class ) );
    assertEquals( run( world,
                       () -> world.createSubscription( "Foo", areaOfInterest ).getName() ),
                  "Foo" );
    assertEquals( createSubscription( world, Collections.singletonList( Component1.class ) ).getName(),
                  "Subscription@2" );

    GaldrTestUtil.disableNames();

    assertInvariantFailure( () -> createSubscription( world, Collections.singletonList( Component1.class ) ).getName(),
                            "Galdr-0004: Subscription.getName() invoked when Galdr.areNamesEnabled() returns false" );
  }

  @Test
  public void toString_test()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final AreaOfInterest areaOfInterest =
      world.createAreaOfInterest( Collections.singletonList( Component1.class ) );

    final Subscription subscription = run( world, () -> world.createSubscription( "MySubscription", areaOfInterest ) );

    assertEquals( subscription.toString(), "Subscription[MySubscription]" );

    GaldrTestUtil.disableDebugToString();

    assertDefaultToString( subscription );
  }

  @Test
  public void dispose()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final Subscription subscription = createSubscription( world, Collections.singletonList( Component1.class ) );

    final EntityCollection collection = subscription.getCollection();

    assertTrue( subscription.isNotDisposed() );
    assertFalse( subscription.isDisposed() );
    assertFalse( collection.isDisposed() );

    assertEquals( world.getSubscriptions().size(), 1 );

    run( world, subscription::dispose );

    assertEquals( world.getSubscriptions().size(), 0 );

    assertFalse( subscription.isNotDisposed() );
    assertTrue( subscription.isDisposed() );
    assertTrue( collection.isDisposed() );
  }

  @Test
  public void dispose_withSpiesEnabled()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final Subscription subscription = createSubscription( world, Collections.singletonList( Component1.class ) );

    final EntityCollection collection = subscription.getCollection();
    final AreaOfInterest areaOfInterest = collection.getAreaOfInterest();

    assertTrue( subscription.isNotDisposed() );
    assertFalse( subscription.isDisposed() );
    assertFalse( collection.isDisposed() );

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( world );
    run( world, subscription::dispose );
    handler.unsubscribe();

    handler.assertEventCount( 4 );
    handler.assertNextEvent( SubscriptionDisposeStartEvent.class, e -> {
      assertEquals( e.getSubscription().getId(), subscription.getId() );
      assertEquals( e.getSubscription().getName(), subscription.getName() );
      assertEquals( e.getSubscription().getCollection().getAreaOfInterest(), areaOfInterest );
    } );
    handler.assertNextEvent( CollectionDisposeStartEvent.class,
                             e -> assertEquals( e.getCollection().getAreaOfInterest(), areaOfInterest ) );
    handler.assertNextEvent( CollectionDisposeCompleteEvent.class,
                             e -> assertEquals( e.getCollection().getAreaOfInterest(), areaOfInterest ) );
    handler.assertNextEvent( SubscriptionDisposeCompleteEvent.class, e -> {
      assertEquals( e.getSubscription().getId(), subscription.getId() );
      assertEquals( e.getSubscription().getName(), subscription.getName() );
      assertEquals( e.getSubscription().getCollection().getAreaOfInterest(), areaOfInterest );
    } );

    assertFalse( subscription.isNotDisposed() );
    assertTrue( subscription.isDisposed() );
    assertTrue( collection.isDisposed() );
  }

  @Test
  public void dispose_withSpiesEnabled_and_collectionSharedWithAnotherubscription()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final Subscription subscription1 = createSubscription( world, Collections.singletonList( Component1.class ) );
    final Subscription subscription2 = createSubscription( world, Collections.singletonList( Component1.class ) );

    final EntityCollection collection = subscription1.getCollection();
    //noinspection SimplifiedTestNGAssertion
    assertTrue( collection == subscription2.getCollection() );

    final AreaOfInterest areaOfInterest = collection.getAreaOfInterest();

    assertTrue( subscription1.isNotDisposed() );
    assertFalse( subscription1.isDisposed() );
    assertFalse( collection.isDisposed() );

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( world );
    run( world, subscription1::dispose );
    handler.unsubscribe();

    handler.assertEventCount( 3 );
    handler.assertNextEvent( SubscriptionDisposeStartEvent.class, e -> {
      assertEquals( e.getSubscription().getId(), subscription1.getId() );
      assertEquals( e.getSubscription().getName(), subscription1.getName() );
      assertEquals( e.getSubscription().getCollection().getAreaOfInterest(), areaOfInterest );
    } );
    handler.assertNextEvent( CollectionDetachEvent.class,
                             e -> assertEquals( e.getCollection().getAreaOfInterest(), areaOfInterest ) );
    handler.assertNextEvent( SubscriptionDisposeCompleteEvent.class, e -> {
      assertEquals( e.getSubscription().getId(), subscription1.getId() );
      assertEquals( e.getSubscription().getName(), subscription1.getName() );
      assertEquals( e.getSubscription().getCollection().getAreaOfInterest(), areaOfInterest );
    } );

    assertFalse( subscription1.isNotDisposed() );
    assertTrue( subscription1.isDisposed() );
    assertFalse( collection.isDisposed() );
  }

  @Test
  public void ensureNotDisposed()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final Subscription subscription = createSubscription( world, Collections.singletonList( Component1.class ) );
    run( world, subscription::dispose );

    assertInvariantFailure( subscription::beginIteration,
                            "Galdr-0043: Attempted to invoke method on Subscription named 'Subscription@1' but the Subscription is disposed." );
    assertInvariantFailure( subscription::abortIteration,
                            "Galdr-0043: Attempted to invoke method on Subscription named 'Subscription@1' but the Subscription is disposed." );
    assertInvariantFailure( subscription::nextEntity,
                            "Galdr-0043: Attempted to invoke method on Subscription named 'Subscription@1' but the Subscription is disposed." );
  }
}
