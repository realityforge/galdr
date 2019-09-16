package galdr;

import java.io.File;
import java.util.Random;
import javax.annotation.Nonnull;
import org.realityforge.braincheck.BrainCheckTestUtil;
import org.realityforge.braincheck.GuardMessageCollector;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import static org.testng.Assert.*;

public abstract class AbstractTest
{
  private static final GuardMessageCollector c_messages = createCollector();
  private final Random _random = new Random();

  @BeforeMethod
  protected void beforeTest()
  {
    BrainCheckTestUtil.resetConfig( false );
    GaldrTestUtil.resetConfig( false );
    c_messages.onTestStart();
  }

  @AfterMethod
  protected void afterTest()
  {
    c_messages.onTestComplete();
    GaldrTestUtil.resetConfig( true );
    BrainCheckTestUtil.resetConfig( true );
  }

  @BeforeSuite
  protected void beforeSuite()
  {
    c_messages.onTestSuiteStart();
  }

  @Nonnull
  private static GuardMessageCollector createCollector()
  {
    final boolean saveIfChanged = "true".equals( System.getProperty( "galdr.output_fixture_data", "false" ) );
    final String fixtureDir = System.getProperty( "galdr.diagnostic_messages_file" );
    assertNotNull( fixtureDir,
                   "Expected System.getProperty( \"galdr.diagnostic_messages_file\" ) to return location of diagnostic messages file" );
    return new GuardMessageCollector( "Galdr", new File( fixtureDir ), saveIfChanged );
  }

  @AfterSuite
  protected void afterSuite()
  {
    if ( !System.getProperty( "galdr.check_diagnostic_messages", "true" ).equals( "false" ) )
    {
      c_messages.onTestSuiteComplete();
    }
  }

  final void assertInvariantFailure( @Nonnull final ThrowingRunnable throwingRunnable, @Nonnull final String message )
  {
    assertEquals( expectThrows( IllegalStateException.class, throwingRunnable ).getMessage(), message );
  }

  protected final int randomInt( final int upperBound )
  {
    return _random.nextInt( upperBound );
  }

  @Nonnull
  final String randomString()
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
