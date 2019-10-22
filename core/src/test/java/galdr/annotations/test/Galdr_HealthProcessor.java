package galdr.annotations.test;

import galdr.Processor;
import galdr.World;
import galdr.internal.OnDeactivateFn;
import galdr.internal.OnActivateFn;
import galdr.internal.PostConstructFn;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated( "galdr" )
final class Galdr_HealthProcessor
  implements Processor, PostConstructFn, OnActivateFn, OnDeactivateFn
{
  @Nonnull
  private final Galdr_HealthProcessorImpl _processor = new Galdr_HealthProcessorImpl( this );

  @Nonnull
  World $galdr$_world()
  {
    return World.current();
  }

  @Override
  public void process( final int delta )
  {
    _processor.$galdr$_process( delta );
  }

  @Override
  public void postConstruct()
  {
    _processor.$galdr$_postConstruct();
  }

  @Override
  public void activate()
  {
    _processor.$galdr$_activate();
  }

  @Override
  public void deactivate()
  {
    _processor.$galdr$_deactivate();
  }
}
