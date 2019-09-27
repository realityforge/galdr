package galdr;

import java.util.Objects;
import java.util.function.Supplier;
import javax.annotation.Nonnull;

/**
 * The class responsible for storing components of a particular type.
 */
abstract class AllocatingComponentManager<T>
  extends ComponentManager<T>
{
  /**
   * Function invoked to create an instance of the component.
   */
  @Nonnull
  private final Supplier<T> _createFn;

  AllocatingComponentManager( @Nonnull final World world,
                              final int id,
                              final int flags,
                              @Nonnull final Class<T> type,
                              @Nonnull final Supplier<T> createFn )
  {
    super( world, id, flags, type );
    _createFn = Objects.requireNonNull( createFn );
  }

  /**
   * Return the function that creates an instance of the component.
   *
   * @return the function that creates an instance of the component.
   */
  @Nonnull
  final Supplier<T> getCreateFn()
  {
    return _createFn;
  }

  /**
   * Create an instance of the component.
   *
   * @return an instance of the component.
   */
  @Nonnull
  final T createComponentInstance()
  {
    return getCreateFn().get();
  }
}
