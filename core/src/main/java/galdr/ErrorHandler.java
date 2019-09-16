package galdr;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Interface for handling errors in processors.
 */
@FunctionalInterface
public interface ErrorHandler
{
  /**
   * Report an error in processor.
   *
   * @param stage     the stage that contained the processor that generated the error.
   * @param processor the processor that generated error.
   * @param throwable the exception that caused error if any.
   */
  void onError( @Nonnull ProcessorStage stage, @Nonnull Processor processor, @Nullable Throwable throwable );
}
