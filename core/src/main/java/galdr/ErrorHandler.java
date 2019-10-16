package galdr;

import grim.annotations.OmitType;
import javax.annotation.Nonnull;

/**
 * Interface for handling errors in processors.
 */
@OmitType( unless = "galdr.enable_error_handlers" )
@FunctionalInterface
public interface ErrorHandler
{
  /**
   * Report an error in processor.
   *
   * @param stage     the stage that contained the processor that generated the error.
   * @param processor the processor that generated error.
   * @param throwable the exception that caused the error.
   */
  void onError( @Nonnull ProcessorStage stage, @Nonnull Processor processor, @Nonnull Throwable throwable );
}
