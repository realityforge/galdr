package galdr.internal;

import javax.annotation.Nullable;

/**
 * Lifecycle interface called to release any resources associated with the subsystem.
 */
@FunctionalInterface
public interface OnDeactivateFn
{
  /**
   * Deactivate the subsystem.
   */
  void deactivate();

  /**
   * Deactivate the supplied object if it implements OnDeactivateFn, else do nothing.
   *
   * @param object the object to deactivate.
   */
  static void deactivate( @Nullable final Object object )
  {
    if ( object instanceof OnDeactivateFn )
    {
      ( (OnDeactivateFn) object ).deactivate();
    }
  }
}
