package galdr.internal;

import javax.annotation.Nullable;

/**
 * Lifecycle interface called to release any resources associated with the element.
 * Once an element is disposed, no methods should be invoked on the element.
 */
@FunctionalInterface
public interface DisposeFn
{
  /**
   * Dispose the element.
   */
  void dispose();

  /**
   * Dispose the supplied object if it is Disposable, else do nothing.
   *
   * @param object the object to dispose.
   */
  static void dispose( @Nullable final Object object )
  {
    if ( object instanceof DisposeFn )
    {
      ( (DisposeFn) object ).dispose();
    }
  }
}
