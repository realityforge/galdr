package galdr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

/**
 * A class containing static methods for creating worlds.
 */
public final class Worlds
{
  private Worlds()
  {
  }

  /**
   * Return a builder to create a world.
   *
   * @return the builder object to create a world.
   */
  @Nonnull
  public static Builder world()
  {
    return world( null );
  }

  /**
   * Return a builder to create a world with the specified name.
   *
   * @param name the human consumable name of the world. MUST be <code>null</code> if {@link Galdr#areNamesEnabled()} returns <code>false</code> otherwise may be non-null.
   * @return the builder object to create a world.
   */
  @Nonnull
  public static Builder world( @Nullable final String name )
  {
    return new Builder( name );
  }

  /**
   * A builder class used to create World instances.
   * An instance of the builder should be created via {@link Worlds#world()} or {@link Worlds#world(String)}.
   */
  public static final class Builder
  {
    static final int DEFAULT_INITIAL_ENTITY_COUNT = 1024;
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
    private int _initialEntityCount = DEFAULT_INITIAL_ENTITY_COUNT;
    /**
     * A flag set to true after build is invoked. After the world is constructed it is invalid to invoke construction methods.
     */
    private boolean _worldConstructed;

    Builder( @Nullable final String name )
    {
      _world = new World( name );
      WorldHolder.activateWorld( _world );
    }

    @Nonnull
    public Builder initialEntityCount( final int initialEntityCount )
    {
      ensureWorldNotConstructed();
      if ( Galdr.shouldCheckApiInvariants() )
      {
        apiInvariant( () -> initialEntityCount > 0,
                      () -> "Galdr-007: Attempted to set initialEntityCount to " + initialEntityCount + " for world " +
                            "named '" + _world.getName() + "' but initialEntityCount must be a positive value." );
      }
      _initialEntityCount = initialEntityCount;
      return this;
    }

    @Nonnull
    public <T> Builder component( @Nonnull final Class<T> type )
    {
      return _component( type, null, ComponentStorage.NONE );
    }

    @Nonnull
    public <T> Builder component( @Nonnull final Class<T> type, @Nonnull final Supplier<T> createFn )
    {
      return _component( type, createFn, ComponentStorage.ARRAY );
    }

    @Nonnull
    public <T> Builder component( @Nonnull final Class<T> type,
                                  @Nonnull final Supplier<T> createFn,
                                  @Nonnull final ComponentStorage storage )
    {
      return _component( type, Objects.requireNonNull( createFn ), storage );
    }

    @Nonnull
    private <T> Builder _component( @Nonnull final Class<T> type,
                                    @Nullable final Supplier<T> createFn,
                                    @Nonnull final ComponentStorage storage )
    {
      ensureWorldNotConstructed();
      _components.add( createComponentManager( _components.size(), type, createFn, storage ) );
      return this;
    }

    @Nonnull
    private <T> ComponentManager<T> createComponentManager( final int componentId,
                                                            @Nonnull final Class<T> type,
                                                            @Nullable final Supplier<T> createFn,
                                                            @Nonnull final ComponentStorage storage )
    {
      if ( ComponentStorage.ARRAY == storage )
      {
        assert null != createFn;
        return new FastArrayComponentManager<>( _world, componentId, type, createFn, _initialEntityCount );
      }
      else if ( ComponentStorage.MAP == storage )
      {
        assert null != createFn;
        return new MapComponentManager<>( _world, componentId, type, createFn );
      }
      else
      {
        assert ComponentStorage.NONE == storage;
        return new NoStorageComponentManager<>( _world, componentId, type );
      }
    }

    @Nonnull
    public Builder stage( @Nonnull final String name, @Nonnull final Processor... processors )
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
      _world.completeConstruction( _initialEntityCount, _components.toArray( new ComponentManager[ 0 ] ), _stages );
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
}
