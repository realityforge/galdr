package galdr.internal;

import javax.annotation.Nullable;

/**
 * Lifecycle interface called to perform custom initialization of the component.
 */
@FunctionalInterface
public interface PostConstructFn
{
  /**
   * Setup the element.
   */
  void postConstruct();

  /**
   * Call postConstruct() on the supplied object if it is PostConstructFn, else do nothing.
   *
   * @param object the object to postConstruct.
   */
  static void postConstruct( @Nullable final Object object )
  {
    if ( object instanceof PostConstructFn )
    {
      ( (PostConstructFn) object ).postConstruct();
    }
  }
}
