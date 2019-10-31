package galdr;

import galdr.internal.OnActivateFn;
import galdr.internal.OnDeactivateFn;
import galdr.internal.PostConstructFn;
import grim.annotations.OmitSymbol;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * An ordered list of {@link Processor} instances that are explicitly invoked by application code.
 */
public final class ProcessorStage
{
  /**
   * The name of the stage.
   */
  @Nonnull
  private final String _name;
  /**
   * The world in which the stage was created.
   */
  @Nonnull
  private final World _world;
  /**
   * The ordered set of processors through which the stage steps.
   */
  @Nonnull
  private final Processor[] _processors;

  ProcessorStage( @Nonnull final String name, @Nonnull final World world, @Nonnull final Processor... processors )
  {
    _name = Objects.requireNonNull( name );
    _world = Objects.requireNonNull( world );
    _processors = Objects.requireNonNull( processors );
  }

  void postConstruct()
  {
    for ( final Processor processor : _processors )
    {
      PostConstructFn.postConstruct( processor );
    }
  }

  void activate()
  {
    //TODO: Set flag when invariants enabled and check in other methods when invariant enabled
    for ( final Processor processor : _processors )
    {
      OnActivateFn.activate( processor );
    }
  }

  void deactivate()
  {
    //TODO: Set flag when invariants enabled and check in other methods when invariant enabled
    for ( final Processor processor : _processors )
    {
      OnDeactivateFn.deactivate( processor );
    }
  }

  public void process( final int delta )
  {
    WorldHolder.run( _world, () -> runStage( delta ) );
  }

  /**
   * Method that invokes each processor in the stage.
   *
   * @param delta the delta supplied to processors when running stage.
   */
  private void runStage( final int delta )
  {
    for ( final Processor processor : _processors )
    {
      try
      {
        processor.process( delta );
      }
      catch ( final Throwable e )
      {
        _world.reportError( this, processor, e );
      }
    }
  }

  /**
   * Return the name of the Stage.
   *
   * @return the name of the Stage.
   */
  @Nonnull
  protected final String getName()
  {
    return _name;
  }

  @OmitSymbol( unless = "galdr.debug_to_string" )
  @Override
  public String toString()
  {
    if ( Galdr.areDebugToStringMethodsEnabled() )
    {
      return "ProcessorStage[" + getName() + "]";
    }
    else
    {
      return super.toString();
    }
  }
}
