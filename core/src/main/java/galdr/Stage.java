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
 * A named, ordered list of subsystems. The application code is responsible for explicitly
 * calling {@link #process(int)} on the stage. This call will then invoke each subsystem in
 * the specified order.
 */
public final class Stage
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
   * The ordered set of SubSystems through which the stage steps.
   */
  @Nonnull
  private final SubSystemEntry[] _subSystems;

  Stage( @Nonnull final String name, @Nonnull final World world, @Nonnull final SubSystemEntry... subSystems )
  {
    _name = Objects.requireNonNull( name );
    _world = Objects.requireNonNull( world );
    _subSystems = Objects.requireNonNull( subSystems );
  }

  void postConstruct()
  {
    for ( final SubSystemEntry entry : _subSystems )
    {
      PostConstructFn.postConstruct( entry.getSubSystem() );
    }
  }

  void activate()
  {
    //TODO: Set flag when invariants enabled and check in other methods when invariant enabled
    for ( final SubSystemEntry entry : _subSystems )
    {
      OnActivateFn.activate( entry.getSubSystem() );
    }
  }

  void deactivate()
  {
    //TODO: Set flag when invariants enabled and check in other methods when invariant enabled
    for ( final SubSystemEntry entry : _subSystems )
    {
      OnDeactivateFn.deactivate( entry.getSubSystem() );
    }
  }

  public void process( final int delta )
  {
    WorldHolder.run( _world, () -> runStage( delta ) );
  }

  /**
   * Method that invokes each SubSystem in the stage.
   *
   * @param delta the delta supplied to SubSystems when running stage.
   */
  private void runStage( final int delta )
  {
    for ( final SubSystemEntry entry : _subSystems )
    {
      final SubSystem subSystem = entry.getSubSystem();
      try
      {
        subSystem.process( delta );
      }
      catch ( final Throwable e )
      {
        _world.reportError( this, Galdr.areNamesEnabled() ? entry.getName() : subSystem.getClass().getSimpleName(), e );
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
      return "Stage[" + getName() + "]";
    }
    else
    {
      return super.toString();
    }
  }

  /**
   * A builder class used to create Stage instances.
   * An instance of the builder by invoking {@link Worlds.Builder#stage(String)}.
   */
  public static final class Builder
  {
    @Nonnull
    private final Worlds.Builder _parent;
    @Nonnull
    private final String _name;
    @Nonnull
    private final List<SubSystemEntry> _subSystems = new ArrayList<>();

    Builder( @Nonnull final Worlds.Builder parent, @Nonnull final String name )
    {
      _parent = Objects.requireNonNull( parent );
      _name = Objects.requireNonNull( name );
    }

    @Nonnull
    public Builder subSystem( @Nonnull final SubSystem subSystem )
    {
      return subSystem( null, subSystem );
    }

    @Nonnull
    public Builder subSystem( @Nullable final String name, @Nonnull final SubSystem subSystem )
    {
      _subSystems.add( new SubSystemEntry( name, subSystem ) );
      return this;
    }

    @Nonnull
    public Worlds.Builder endStage()
    {
      return _parent.stage( _name, _subSystems.toArray( new SubSystemEntry[ 0 ] ) );
    }
  }
}
