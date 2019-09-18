package galdr;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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

  ProcessorStage( @Nullable final String name, @Nonnull final World world, @Nonnull final Processor... processors )
  {
    _name = Objects.requireNonNull( name );
    _world = Objects.requireNonNull( world );
    if ( Galdr.shouldCopyArraysPassedToConstructors() )
    {
      _processors = new Processor[ processors.length ];
      for ( int i = 0; i < _processors.length; i++ )
      {
        _processors[ i ] = Objects.requireNonNull( processors[ i ] );
      }
    }
    else
    {
      _processors = processors;
    }
  }

  public void process( final int delta )
  {
    WorldHolder.activateWorld( _world );
    try
    {
      for ( final Processor processor : _processors )
      {
        try
        {
          processor.process( delta );
        }
        catch ( final Throwable e )
        {
          //TODO: Deliver error to per stage or world-global error handler?
        }
      }
    }
    finally
    {
      WorldHolder.deactivateWorld( _world );
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
