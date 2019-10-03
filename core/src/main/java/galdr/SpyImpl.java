package galdr;

import galdr.spy.Spy;
import galdr.spy.SpyEventHandler;
import javax.annotation.Nonnull;

/**
 * Spy implementation.
 */
final class SpyImpl
  implements Spy
{
  /**
   * Support infrastructure for interacting with spy event handlers..
   */
  @Nonnull
  private final SpyEventHandlerSupport _spyEventHandlerSupport = new SpyEventHandlerSupport();

  SpyImpl()
  {
  }

  @Override
  public void addSpyEventHandler( @Nonnull final SpyEventHandler handler )
  {
    _spyEventHandlerSupport.addSpyEventHandler( handler );
  }

  @Override
  public void removeSpyEventHandler( @Nonnull final SpyEventHandler handler )
  {
    _spyEventHandlerSupport.removeSpyEventHandler( handler );
  }

  @Override
  public void reportSpyEvent( @Nonnull final Object event )
  {
    _spyEventHandlerSupport.reportSpyEvent( event );
  }

  @Override
  public boolean willPropagateSpyEvents()
  {
    return _spyEventHandlerSupport.willPropagateSpyEvents();
  }
}
