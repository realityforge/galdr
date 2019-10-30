package galdr;

import galdr.spy.Spy;
import galdr.spy.SubscriptionInfo;
import java.util.Collections;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class SubscriptionInfoImplTest
  extends AbstractTest
{
  private static class Component1
  {
  }

  @Test
  public void basicOperation()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final Spy spy = world.getSpy();

    assertEquals( spy.getSubscriptions().size(), 0 );

    final Subscription subscription = createSubscription( world, Collections.singletonList( Component1.class ) );

    assertEquals( spy.getSubscriptions().size(), 1 );

    final SubscriptionInfo info = spy.findSubscriptionById( subscription.getId() );

    assertNotNull( info );
    assertEquals( info.getId(), subscription.getId() );
    assertEquals( info.getName(), subscription.getName() );
    assertEquals( info.getCollection().getAreaOfInterest(), subscription.getCollection().getAreaOfInterest() );
    assertFalse( info.isDisposed() );

    run( world, subscription::dispose );

    assertEquals( spy.getSubscriptions().size(), 0 );
    assertTrue( info.isDisposed() );
    assertNull( spy.findSubscriptionById( subscription.getId() ) );
  }

  @Test
  public void spyDisabled()
  {
    GaldrTestUtil.disableSpies();

    final World world = Worlds.world().component( Component1.class ).build();
    final Subscription subscription = createSubscription( world, Collections.singletonList( Component1.class ) );

    assertInvariantFailure( subscription::asInfo,
                            "Galdr-0040: Subscription.asInfo() invoked but Galdr.areSpiesEnabled() returned false." );
  }
}
