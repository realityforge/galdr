package galdr;

import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import org.realityforge.braincheck.BrainCheckTestUtil;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import static org.testng.Assert.*;

@Listeners( MessageCollector.class )
public abstract class AbstractTest
{
  @Nonnull
  private final Random _random = new Random();
  @Nonnull
  private final TestLogger _logger = new TestLogger();

  @BeforeMethod
  protected void beforeTest()
  {
    BrainCheckTestUtil.resetConfig( false );
    GaldrTestUtil.resetConfig( false );
    _logger.getEntries().clear();
    GaldrTestUtil.setLogger( _logger );
  }

  @AfterMethod
  protected void afterTest()
  {
    GaldrTestUtil.resetConfig( true );
    BrainCheckTestUtil.resetConfig( true );
  }

  @Nonnull
  final TestLogger getTestLogger()
  {
    return _logger;
  }

  protected final void run( @Nonnull final World world, @Nonnull final Runnable action )
  {
    WorldHolder.run( world, action::run );
  }

  protected final <T> T run( @Nonnull final World world, @Nonnull final Supplier<T> action )
  {
    return WorldHolder.run( world, action::get );
  }

  protected final int createEntity( @Nonnull final World world, @Nonnull final Class<?>... componentTypes )
  {
    return run( world, () -> world.createEntity( componentTypes ) );
  }

  final boolean isAlive( @Nonnull final World world, final int entityId )
  {
    return run( world, () -> world.isAlive( entityId ) );
  }

  @Nonnull
  protected final Subscription createSubscription( @Nonnull final World world, @Nonnull final Collection<Class<?>> all )
  {
    return run( world, () -> world.createSubscription( world.createAreaOfInterest( all,
                                                                                   Collections.emptyList(),
                                                                                   Collections.emptyList() ) ) );
  }

  final void assertDefaultToString( @Nonnull final Object object )
  {
    assertEquals( object.toString(),
                  object.getClass().getName() + "@" + Integer.toHexString( object.hashCode() ) );
  }

  protected final void assertInvariantFailure( @Nonnull final ThrowingRunnable throwingRunnable,
                                               @Nonnull final String message )
  {
    assertEquals( expectThrows( IllegalStateException.class, throwingRunnable ).getMessage(), message );
  }

  protected final int randomInt( final int upperBound )
  {
    return _random.nextInt( upperBound );
  }

  @Nonnull
  protected final BitSet set( @Nonnull final int... bits )
  {
    final BitSet set = new BitSet();
    for ( final int bit : bits )
    {
      set.set( bit );
    }
    return set;
  }

  @Nonnull
  protected final String randomString()
  {
    return randomString( 8 );
  }

  @SuppressWarnings( "SameParameterValue" )
  @Nonnull
  private String randomString( final int length )
  {
    final String stringCharacters = "abcdefghijklmnopqrstuvwxyz0123456789";
    final StringBuilder sb = new StringBuilder( length );
    for ( int i = 0; i < length; i++ )
    {
      sb.append( stringCharacters.charAt( _random.nextInt( length ) ) );
    }
    return sb.toString();
  }
}
