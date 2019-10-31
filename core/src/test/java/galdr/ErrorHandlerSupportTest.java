package galdr;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ErrorHandlerSupportTest
  extends AbstractTest
{
  @Test
  public void basicOperation()
  {
    final ErrorHandlerSupport support = new ErrorHandlerSupport();

    final Throwable throwable = new Throwable();

    final String processorName = randomString();
    final ProcessorStage stage = createStage( processorName );

    final AtomicInteger callCount = new AtomicInteger();

    final ErrorHandler handler = ( stageArg, processorArg, throwableArg ) -> {
      callCount.incrementAndGet();
      assertEquals( stageArg, stage );
      assertEquals( processorArg, processorName );
      assertEquals( throwableArg, throwable );
    };
    support.addErrorHandler( handler );
    assertEquals( support.getHandlers().size(), 1 );

    support.onError( stage, processorName, throwable );

    assertEquals( callCount.get(), 1 );

    support.onError( stage, processorName, throwable );

    assertEquals( callCount.get(), 2 );

    support.removeErrorHandler( handler );

    assertEquals( support.getHandlers().size(), 0 );

    support.onError( stage, processorName, throwable );

    // Not called again
    assertEquals( callCount.get(), 2 );
  }

  @Test
  public void addErrorHandler_alreadyExists()
  {
    final ErrorHandlerSupport support = new ErrorHandlerSupport();

    final ErrorHandler handler = ( stage, processor, throwable ) -> {
    };
    support.addErrorHandler( handler );

    assertInvariantFailure( () -> support.addErrorHandler( handler ),
                            "Galdr-0096: Attempting to add handler " + handler + " that is already in " +
                            "the list of error handlers." );
  }

  @Test
  public void removeErrorHandler_noExists()
  {
    final ErrorHandlerSupport support = new ErrorHandlerSupport();

    final ErrorHandler handler = ( stage, processor, throwable ) -> {
    };

    assertInvariantFailure( () -> support.removeErrorHandler( handler ),
                            "Galdr-0097: Attempting to remove handler " + handler + " that is not in " +
                            "the list of error handlers." );
  }

  @Test
  public void multipleHandlers()
  {
    final ErrorHandlerSupport support = new ErrorHandlerSupport();

    final String processorName = randomString();
    final ProcessorStage stage = createStage( processorName );
    final Throwable throwable = new Throwable();

    final AtomicInteger callCount1 = new AtomicInteger();
    final AtomicInteger callCount2 = new AtomicInteger();
    final AtomicInteger callCount3 = new AtomicInteger();

    final ErrorHandler handler1 = ( stageArg, processorArg, throwableArg ) -> callCount1.incrementAndGet();
    final ErrorHandler handler2 = ( stageArg, processorArg, throwableArg ) -> callCount2.incrementAndGet();
    final ErrorHandler handler3 = ( stageArg, processorArg, throwableArg ) -> callCount3.incrementAndGet();
    support.addErrorHandler( handler1 );
    support.addErrorHandler( handler2 );
    support.addErrorHandler( handler3 );

    assertEquals( support.getHandlers().size(), 3 );

    support.onError( stage, processorName, throwable );

    assertEquals( callCount1.get(), 1 );
    assertEquals( callCount2.get(), 1 );
    assertEquals( callCount3.get(), 1 );

    support.onError( stage, processorName, throwable );

    assertEquals( callCount1.get(), 2 );
    assertEquals( callCount2.get(), 2 );
    assertEquals( callCount3.get(), 2 );
  }

  @Test
  public void onError_whereOneHandlerGeneratesError()
  {
    final ErrorHandlerSupport support = new ErrorHandlerSupport();

    final String processorName = randomString();
    final ProcessorStage stage = createStage( processorName );
    final Throwable throwable = new Throwable();

    final AtomicInteger callCount1 = new AtomicInteger();
    final AtomicInteger callCount3 = new AtomicInteger();

    final RuntimeException exception = new RuntimeException( "X" );

    final ErrorHandler handler1 = ( stageArg, processorArg, throwableArg ) -> callCount1.incrementAndGet();
    final ErrorHandler handler2 = ( stageArg, processorArg, throwableArg ) -> {
      throw exception;
    };
    final ErrorHandler handler3 = ( stageArg, processorArg, throwableArg ) -> callCount3.incrementAndGet();
    support.addErrorHandler( handler1 );
    support.addErrorHandler( handler2 );
    support.addErrorHandler( handler3 );

    support.onError( stage, processorName, throwable );

    assertEquals( callCount1.get(), 1 );
    assertEquals( callCount3.get(), 1 );

    final ArrayList<TestLogger.LogEntry> entries = getTestLogger().getEntries();
    assertEquals( entries.size(), 1 );
    final TestLogger.LogEntry entry1 = entries.get( 0 );
    assertEquals( entry1.getMessage(),
                  "Exception when notifying error handler '" + handler2 + "' of error in processor named '" +
                  processorName + "' in stage named '" + stage.getName() + "'." );
    assertEquals( entry1.getThrowable(), exception );

    support.onError( stage, processorName, throwable );

    assertEquals( callCount1.get(), 2 );
    assertEquals( callCount3.get(), 2 );

    assertEquals( getTestLogger().getEntries().size(), 2 );
  }

  @Test
  public void onError_whereOneHandlerGeneratesError_but_Galdr_areNamesEnabled_is_false()
  {
    GaldrTestUtil.disableNames();

    final ErrorHandlerSupport support = new ErrorHandlerSupport();

    final String processorName = randomString();
    final ProcessorStage stage = createStage( processorName );
    final Throwable throwable = new Throwable();

    final RuntimeException exception = new RuntimeException( "X" );

    final ErrorHandler handler2 = ( stageArg, processorArg, throwableArg ) -> {
      throw exception;
    };
    support.addErrorHandler( handler2 );

    support.onError( stage, processorName, throwable );

    final ArrayList<TestLogger.LogEntry> entries = getTestLogger().getEntries();
    assertEquals( entries.size(), 1 );
    final TestLogger.LogEntry entry1 = entries.get( 0 );
    assertEquals( entry1.getMessage(), "Error triggered when invoking ErrorHandler.onError()" );
    assertEquals( entry1.getThrowable(), exception );

    support.onError( stage, processorName, throwable );

    assertEquals( getTestLogger().getEntries().size(), 2 );
  }

  @Nonnull
  private ProcessorStage createStage( @Nonnull final String processorName )
  {
    final String stageName = randomString();
    final World world =
      Worlds.world().stage( stageName ).processor( processorName, new BasicNoopProcessor() ).endStage().build();
    return world.getStageByName( stageName );
  }
}
