package galdr;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class SubscriptionTest
  extends AbstractTest
{
  private static class Component1
  {
  }

  @Test
  public void create()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final AreaOfInterest areaOfInterest = new AreaOfInterest( set( 0 ), set(), set() );

    assertEquals( world.getSubscriptions().size(), 0 );
    final Subscription subscription = world.createSubscription( areaOfInterest );

    assertEquals( world.getSubscriptions().size(), 1 );
    assertEquals( world.getSubscriptions().get( areaOfInterest ), subscription );

    assertEquals( subscription.getAreaOfInterest(), areaOfInterest );
    assertEquals( subscription.getEntities().cardinality(), 0 );
    assertEquals( subscription.getNewEntities().cardinality(), 0 );
    assertEquals( subscription.getCurrentEntityId(), -1 );
    assertEquals( subscription.getFlags(), 0 );
    assertTrue( subscription.isNotDisposed() );
    assertFalse( subscription.isDisposed() );
    assertFalse( subscription.isProcessingNewEntities() );
    assertFalse( subscription.hasNewEntities() );
  }

  @Test
  public void ensureNotDisposed()
  {
    final World world = Worlds.world().build();

    final Subscription subscription = world.createSubscription( new AreaOfInterest( set(), set(), set() ) );

    subscription.ensureNotDisposed();

    subscription.markAsDisposed();

    assertInvariantFailure( subscription::ensureNotDisposed,
                            "Galdr-0015: Invoked method on Subscription when subscription is disposed." );
  }
}
