package galdr;

import grim.annotations.OmitType;
import javax.annotation.Nonnull;

/**
 * Interface for handling errors in SubSystems.
 */
@OmitType( unless = "galdr.enable_error_handlers" )
@FunctionalInterface
public interface ErrorHandler
{
  /**
   * Report an error in a SubSystem.
   *
   * @param stage         the stage that contained the SubSystem that generated the error.
   * @param subSystemName the name of the {@link SubSystem} that generated the error.
   * @param throwable     the exception that caused the error.
   */
  void onError( @Nonnull Stage stage, @Nonnull String subSystemName, @Nonnull Throwable throwable );
}
