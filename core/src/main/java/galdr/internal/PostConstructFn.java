package galdr.internal;

import javax.annotation.Nullable;

/**
 * Lifecycle interface called to allocate any resources required to invoke the subsystem that persist even when subsystem is deativated.
 */
@FunctionalInterface
public interface PostConstructFn
{
  /**
   * Invoked after the subsystem is constructed.
   */
  void postConstruct();

  /**
   * Call postConstruct() on the supplied object if it implements {@link PostConstructFn}, else do nothing.
   *
   * @param object the object to activate.
   */
  static void postConstruct( @Nullable final Object object )
  {
    if ( object instanceof PostConstructFn )
    {
      ( (PostConstructFn) object ).postConstruct();
    }
  }
}
