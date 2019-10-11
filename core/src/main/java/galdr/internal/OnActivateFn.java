package galdr.internal;

import javax.annotation.Nullable;

/**
 * Lifecycle interface called to allocate any resources required to invoke the subsystem.
 */
@FunctionalInterface
public interface OnActivateFn
{
  /**
   * Activate the subsystem.
   */
  void activate();

  /**
   * Call activate() on the supplied object if it implements {@link OnActivateFn}, else do nothing.
   *
   * @param object the object to activate.
   */
  static void activate( @Nullable final Object object )
  {
    if ( object instanceof OnActivateFn )
    {
      ( (OnActivateFn) object ).activate();
    }
  }
}
