package galdr;

import grim.annotations.OmitSymbol;
import grim.annotations.OmitType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import static org.realityforge.braincheck.Guards.*;

/**
 * Class supporting the propagation of errors for ErrorHandler callback to multiple error handlers.
 */
@OmitType( unless = "galdr.enable_error_handlers" )
final class ErrorHandlerSupport
  implements ErrorHandler
{
  /**
   * The list of error handlers to call when an error is received.
   */
  @Nonnull
  private final List<ErrorHandler> _handlers = new ArrayList<>();

  /**
   * Add error handler to the list of error handlers called.
   * The handler should not already be in the list.
   *
   * @param handler the error handler.
   */
  void addErrorHandler( @Nonnull final ErrorHandler handler )
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> !_handlers.contains( handler ),
                    () -> "Galdr-0096: Attempting to add handler " + handler + " that is already in " +
                          "the list of error handlers." );
    }
    _handlers.add( Objects.requireNonNull( handler ) );
  }

  /**
   * Remove error handler from list of existing error handlers.
   * The handler should already be in the list.
   *
   * @param handler the error handler.
   */
  void removeErrorHandler( @Nonnull final ErrorHandler handler )
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> _handlers.contains( handler ),
                    () -> "Galdr-0097: Attempting to remove handler " + handler + " that is not in " +
                          "the list of error handlers." );
    }
    _handlers.remove( Objects.requireNonNull( handler ) );
  }

  @Override
  public void onError( @Nonnull final Stage stage,
                       @Nonnull final String subSystemName,
                       @Nonnull final Throwable throwable )
  {
    for ( final ErrorHandler errorHandler : _handlers )
    {
      try
      {
        errorHandler.onError( stage, subSystemName, throwable );
      }
      catch ( final Throwable nestedError )
      {
        if ( Galdr.areNamesEnabled() )
        {
          final String message =
            GaldrUtil.safeGetString( () -> "Exception when notifying error handler '" + errorHandler + "' of error " +
                                           "in SubSystem named '" + subSystemName + "' in stage named '" +
                                           stage.getName() + "'." );
          GaldrLogger.log( message, nestedError );
        }
        else
        {
          GaldrLogger.log( "Error triggered when invoking ErrorHandler.onError()", nestedError );
        }
      }
    }
  }

  @OmitSymbol
  @Nonnull
  List<ErrorHandler> getHandlers()
  {
    return _handlers;
  }
}
