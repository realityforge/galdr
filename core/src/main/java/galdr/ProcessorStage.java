package galdr;

import galdr.internal.OnActivateFn;
import galdr.internal.OnDeactivateFn;
import galdr.internal.PostConstructFn;
import grim.annotations.OmitSymbol;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * An ordered list of subsystems. These stage is explicitly invoked by application code.
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
  private final ProcessorEntry[] _processors;

  ProcessorStage( @Nonnull final String name, @Nonnull final World world, @Nonnull final ProcessorEntry... processors )
  {
    _name = Objects.requireNonNull( name );
    _world = Objects.requireNonNull( world );
    _processors = Objects.requireNonNull( processors );
  }

  void postConstruct()
  {
    for ( final ProcessorEntry entry : _processors )
    {
      PostConstructFn.postConstruct( entry.getProcessor() );
    }
  }

  void activate()
  {
    //TODO: Set flag when invariants enabled and check in other methods when invariant enabled
    for ( final ProcessorEntry entry : _processors )
    {
      OnActivateFn.activate( entry.getProcessor() );
    }
  }

  void deactivate()
  {
    //TODO: Set flag when invariants enabled and check in other methods when invariant enabled
    for ( final ProcessorEntry entry : _processors )
    {
      OnDeactivateFn.deactivate( entry.getProcessor() );
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
    for ( final ProcessorEntry entry : _processors )
    {
      final ProcessorFn processor = entry.getProcessor();
      try
      {
        processor.process( delta );
      }
      catch ( final Throwable e )
      {
        _world.reportError( this, Galdr.areNamesEnabled() ? entry.getName() : processor.getClass().getSimpleName(), e );
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

  /**
   * A builder class used to create ProcessorStage instances.
   * An instance of the builder by invoking {@link Worlds.Builder#stage(String)}.
   */
  public static final class Builder
  {
    @Nonnull
    private final Worlds.Builder _parent;
    @Nonnull
    private final String _name;
    @Nonnull
    private final List<ProcessorEntry> _processors = new ArrayList<>();

    Builder( @Nonnull final Worlds.Builder parent, @Nonnull final String name )
    {
      _parent = Objects.requireNonNull( parent );
      _name = Objects.requireNonNull( name );
    }

    @Nonnull
    public Builder processor( @Nonnull final ProcessorFn processor )
    {
      return processor( null, processor );
    }

    @Nonnull
    public Builder processor( @Nullable final String name, @Nonnull final ProcessorFn processor )
    {
      _processors.add( new ProcessorEntry( name, processor ) );
      return this;
    }

    @Nonnull
    public Worlds.Builder endStage()
    {
      return _parent.stage( _name, _processors.toArray( new ProcessorEntry[ 0 ] ) );
    }
  }
}
