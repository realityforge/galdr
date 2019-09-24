package galdr;

import java.util.ArrayList;
import java.util.Random;
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
  @Nonnull
  private final ArrayList<String> _errors = new ArrayList<>();
  @Nonnull
  private final ErrorHandler _errorHandler = this::onProcessorError;
  private boolean _ignoreErrors;

  @BeforeMethod
  protected void beforeTest()
  {
    BrainCheckTestUtil.resetConfig( false );
    GaldrTestUtil.resetConfig( false );
    _logger.getEntries().clear();
    GaldrTestUtil.setLogger( _logger );
    _ignoreErrors = false;
    _errors.clear();
  }

  @AfterMethod
  protected void afterTest()
  {
    GaldrTestUtil.resetConfig( true );
    BrainCheckTestUtil.resetConfig( true );
    if ( !_ignoreErrors && !_errors.isEmpty() )
    {
      fail( "Unexpected Processor Errors: " + String.join( "\n", _errors ) );
    }
  }

  @Nonnull
  final TestLogger getTestLogger()
  {
    return _logger;
  }

  @Nonnull
  protected final ErrorHandler getErrorHandler()
  {
    return _errorHandler;
  }

  protected final void ignoreProcessorErrors()
  {
    _ignoreErrors = true;
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

  private void onProcessorError( @Nonnull final ProcessorStage stage,
                                 @Nonnull final Processor processor,
                                 @Nonnull final Throwable throwable )
  {
    final String message = "Stage: " + stage.getName() + " Processor: " + processor.getName() + " " + throwable;
    _errors.add( message );
    if ( !_ignoreErrors )
    {
      System.out.println( message );
      throwable.printStackTrace();
    }
  }
}
