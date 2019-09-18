package galdr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

public final class WorldBuilder
{
  /**
   * The world that is being setup by the builder.
   */
  @Nonnull
  private final World _world;
  /**
   * The set of components that will be defined in the world.
   */
  @Nonnull
  private final List<ComponentManager<?>> _components = new ArrayList<>();
  /**
   * The set of components that will be defined in the world.
   */
  @Nonnull
  private final Map<String, ProcessorStage> _stages = new HashMap<>();
  /**
   * A flag set to true after build is invoked. After the world is constructed it is invalid to invoke construction methods.
   */
  private boolean _worldConstructed;

  WorldBuilder( @Nullable final String name )
  {
    _world = new World( name );
    WorldHolder.activateWorld( _world );
  }

  @Nonnull
  public <T> WorldBuilder component( @Nonnull final Class<T> type, @Nonnull final Supplier<T> createFn )
  {
    ensureWorldNotConstructed();
    _components.add( new FastArrayComponentManager<>( _components.size(), type, createFn ) );
    return this;
  }

  @Nonnull
  public <T> WorldBuilder stage( @Nonnull final String name, @Nonnull final Processor... processors )
  {
    ensureWorldNotConstructed();
    if ( Galdr.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> !_stages.containsKey( name ),
                    () -> "Galdr-0087: Attempted to create stage named named '" +
                          name + "' but a stage already exists with the specified name. Existing stages include: " +
                          _stages.keySet().stream().sorted().collect( Collectors.toList() ) );
    }
    assert !_stages.containsKey( name );
    _stages.put( name, new ProcessorStage( name, _world, processors ) );
    return this;
  }

  @Nonnull
  public World build()
  {
    ensureWorldNotConstructed();
    _worldConstructed = true;
    WorldHolder.deactivateWorld( _world );
    final ComponentRegistry registry = new ComponentRegistry( _components.toArray( new ComponentManager[ 0 ] ) );
    _world.completeConstruction( registry, _stages );
    return _world;
  }

  private void ensureWorldNotConstructed()
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      invariant( () -> !_worldConstructed,
                 () -> "Galdr-0019: Attempted to invoke method on WorldBuilder but world has already been constructed" );
    }
  }
}
