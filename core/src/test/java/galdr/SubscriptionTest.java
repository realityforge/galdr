package galdr;

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

    final Subscription subscription = run( world, () -> world.createSubscription( areaOfInterest ) );

    assertEquals( subscription.getName(), "Subscription@1" );
    assertEquals( subscription.getCollection().getAreaOfInterest(), areaOfInterest );
  }

  @Test
  public void construct_namedPassedButNamesDisabled()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final Subscription subscription = createSubscription( world, Collections.singletonList( Component1.class ) );
    GaldrTestUtil.disableNames();
    assertInvariantFailure( () -> new Subscription( "MyName", subscription.getCollection() ),
                            "Galdr-0052: Subscription passed a name 'MyName' but Galdr.areNamesEnabled() is false" );
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
                  "Subscription@1" );

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

    run( world, subscription::dispose );

    assertFalse( subscription.isNotDisposed() );
    assertTrue( subscription.isDisposed() );
    assertTrue( collection.isDisposed() );
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
