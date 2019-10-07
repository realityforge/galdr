package galdr;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class SubscriptionTest
  extends AbstractTest
{
  @Test
  public void construct()
  {
    final World world = Worlds.world().build();
    final AreaOfInterest areaOfInterest = world.createAreaOfInterest( set(), set(), set() );

    final Subscription subscription = run( world, () -> world.createSubscription( areaOfInterest ) );

    assertEquals( subscription.getName(), "Subscription@1" );
    assertEquals( subscription.getCollection().getAreaOfInterest(), areaOfInterest );
  }

  @Test
  public void construct_namedPassedButNamesDisabled()
  {
    final World world = Worlds.world().build();
    final Subscription subscription = createSubscription( world, set(), set(), set() );
    GaldrTestUtil.disableNames();
    assertInvariantFailure( () -> new Subscription( "MyName", subscription.getCollection() ),
                            "Galdr-0052: Subscription passed a name 'MyName' but Galdr.areNamesEnabled() is false" );
  }

  @Test
  public void toString_test()
  {
    final World world = Worlds.world().build();
    final AreaOfInterest areaOfInterest = world.createAreaOfInterest( set(), set(), set() );

    final Subscription subscription = run( world, () -> world.createSubscription( "MySubscription", areaOfInterest ) );

    assertEquals( subscription.toString(), "Subscription[MySubscription]" );

    GaldrTestUtil.disableDebugToString();

    assertDefaultToString( subscription );
  }
}
