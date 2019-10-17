package galdr;

import galdr.spy.ComponentInfo;
import galdr.spy.Spy;
import galdr.spy.SpyEventHandler;
import galdr.spy.WorldInfo;
import grim.annotations.OmitType;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * Spy implementation.
 */
@OmitType( unless = "galdr.enable_spies" )
final class SpyImpl
  implements Spy
{
  /**
   * The world that this spy is associated wtih.
   */
  @Nonnull
  private final World _world;
  /**
   * Support infrastructure for interacting with spy event handlers..
   */
  @Nonnull
  private final SpyEventHandlerSupport _spyEventHandlerSupport = new SpyEventHandlerSupport();

  SpyImpl( @Nonnull final World world )
  {
    _world = Objects.requireNonNull( world );
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

  @Nonnull
  @Override
  public WorldInfo asWorldInfo()
  {
    return _world.asInfo();
  }

  @Nonnull
  @Override
  public ComponentInfo getComponentByType( @Nonnull final Class<?> componentType )
  {
    return _world.getComponentManagerByType( componentType ).asInfo();
  }
}
