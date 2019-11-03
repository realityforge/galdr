package galdr.annotations.test;

import galdr.ProcessorFn;
import galdr.World;
import galdr.internal.PostConstructFn;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated( "galdr" )
final class Galdr_BootstrapProcessor
  implements ProcessorFn, PostConstructFn
{
  @Nonnull
  private final Galdr_BootstrapProcessorImpl _processor = new Galdr_BootstrapProcessorImpl( this );

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
}
