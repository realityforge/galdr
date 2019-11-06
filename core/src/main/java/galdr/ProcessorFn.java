package galdr;

/**
 * Interface implement for elements responsible for updating the state of the world.
 */
@FunctionalInterface
public interface ProcessorFn
{
  /**
   * The method called to allow this processor to update the world.
   * The delta is a positive value indicating the difference in logical time between this
   * update and the previous update. The actual units are arbitrarily determined by the
   * application that invokes the {@link ProcessorStage#process(int)} on the containing
   * stage.
   *
   * @param delta the delta between this call and the previous call.
   */
  void process( int delta );
}