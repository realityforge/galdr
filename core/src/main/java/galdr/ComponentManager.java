package galdr;

import galdr.spy.ComponentAddCompleteEvent;
import galdr.spy.ComponentAddStartEvent;
import galdr.spy.ComponentInfo;
import galdr.spy.ComponentRemoveStartEvent;
import grim.annotations.OmitSymbol;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

/**
 * The class responsible for storing components of a particular type.
 * The A
 * A non-obvious aspect of the API is that the entityId parameter passed into the methods MUST
 * refer to an allocated and alive Entity instance.
 *
 * @param <T> the type of the component.
 */
public abstract class ComponentManager<T>
{
  /**
   * The world containing the component.
   */
  @Nonnull
  private final World _world;
  /**
   * Unique id of type within a {@link World}.
   * The id also serves as the index into the underlying array and the bit used to mark entity as containing component.
   * Used to enable fast lookup and access of component data.
   */
  private final int _id;
  /**
   * Flags used to control the behaviour of the component manager.
   */
  private final int _flags;
  /**
   * The java type of the component.
   */
  @Nonnull
  private final Class<T> _type;
  /**
   * The collections that have an {@link AreaOfInterest} that overlaps with this component.
   */
  @Nonnull
  private final List<EntityCollection> _collections = new ArrayList<>();
  /**
   * Cached info object associated with element.
   * This should be null if {@link Galdr#areSpiesEnabled()} is false.
   */
  @OmitSymbol( unless = "galdr.enable_spies" )
  @Nullable
  private ComponentInfoImpl _info;

  ComponentManager( @Nonnull final World world,
                    final int id,
                    final int flags,
                    @Nonnull final Class<T> type )
  {
    _world = Objects.requireNonNull( world );
    _id = id;
    _flags = flags;
    _type = Objects.requireNonNull( type );
  }

  /**
   * Return the world containing the component.
   *
   * @return the world containing the component.
   */
  @Nonnull
  World getWorld()
  {
    return _world;
  }

  /**
   * Return the storage strategy used by the component.
   *
   * @return the storage strategy used by the component.
   */
  @Nonnull
  public abstract ComponentStorage getStorage();

  /**
   * Return the unique id of the component within the containing {@link World}.
   *
   * @return the unique id of the component within the containing {@link World}.
   */
  public int getId()
  {
    return _id;
  }

  /**
   * Return the java type of the component.
   *
   * @return the java type of the component.
   */
  @Nonnull
  final Class<T> getType()
  {
    return _type;
  }

  /**
   * Return the human readable name of the ComponentManager.
   * This method should NOT be invoked unless {@link Galdr#areNamesEnabled()} returns <code>true</code>.
   *
   * @return the human readable name of the ComponentManager.
   */
  @OmitSymbol( unless = "galdr.enable_names" )
  @Nonnull
  String getName()
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      apiInvariant( Galdr::areNamesEnabled,
                    () -> "Galdr-0004: ComponentManager.getName() invoked when Galdr.areNamesEnabled() returns false" );
    }
    return _type.getSimpleName();
  }

  /**
   * Return true if the specified entity has a component contained in this store.
   * This method MUST NOT return <code>true</code> if the component is deleted or scheduled for deletion.
   *
   * @param entityId the id of the entity.
   * @return true if the specified entity has a component contained in this store, false otherwise.
   */
  public final boolean has( final int entityId )
  {
    ensureCurrentWorldMatches();
    return getEntityById( entityId ).getComponentIds().get( _id );
  }

  @Nonnull
  private Entity getEntityById( final int entityId )
  {
    return _world.getEntityManager().getEntityById( entityId );
  }

  /**
   * Return the component instance for the specified entity, if the component exists.
   *
   * @param entityId the id of the entity.
   * @return the component instance if it exists.
   */
  @Nullable
  public final T find( final int entityId )
  {
    ensureCurrentWorldMatches();
    return has( entityId ) ? performGet( entityId ) : null;
  }

  /**
   * Template method implemented by the ComponentManager implementation to get the component for entity.
   *
   * @param entityId the id of the entity.
   */
  @Nonnull
  abstract T performGet( int entityId );

  /**
   * Return the component instance for the specified entity.
   * The component is expected to exist for the entity and it is an error to invoke
   * this method when the component does not exist.
   *
   * @param entityId the id of the entity.
   * @return the component instance.
   */
  @Nonnull
  public final T get( final int entityId )
  {
    ensureCurrentWorldMatches();
    if ( Galdr.shouldCheckApiInvariants() )
    {
      final boolean isPresent = has( entityId );
      apiInvariant( () -> isPresent,
                    () -> "Galdr-0033: The ComponentManager.get() method for the component named '" + getName() + "' " +
                          "expected to find a component for entityId " + entityId + " but is unable to " +
                          "locate component." );
    }
    return performGet( entityId );
  }

  /**
   * Find the component instance or create a new one if it does not exist.
   *
   * @param entityId the id of the entity.
   * @return the component instance.
   */
  @Nonnull
  public final T findOrCreate( final int entityId )
  {
    ensureCurrentWorldMatches();
    final T component = find( entityId );
    return null == component ? create( entityId ) : component;
  }

  /**
   * Create the component instance for the specified entity.
   *
   * @param entityId the id of the entity.
   * @return the component instance.
   */
  @Nonnull
  public final T create( final int entityId )
  {
    ensureCurrentWorldMatches();
    final T component = newComponent( entityId, true );
    assert null != component;
    return component;
  }

  /**
   * Allocate a component instance for the specified entity.
   * Unlike the {@link #create(int)} and {@link #findOrCreate(int)}, this method does not return the component.
   * It is expected that this method will be used for components that have no state.
   *
   * @param entityId the id of the entity.
   */
  public final void allocate( final int entityId )
  {
    ensureCurrentWorldMatches();
    newComponent( entityId, false );
  }

  @Nullable
  private T newComponent( final int entityId, final boolean shouldReturnComponent )
  {
    final Entity entity = getEntityById( entityId );
    final BitSet componentIds = entity.getComponentIds();
    if ( Galdr.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> !componentIds.get( _id ),
                    () -> "Galdr-0031: The ComponentManager.create() method invoked but entity " + entityId +
                          " already has the component named '" + getName() + "'." );
    }
    if ( entity.isNotAdding() && _world.willPropagateSpyEvents() )
    {
      _world.getSpy().reportSpyEvent( new ComponentAddStartEvent( _world, entity.getId(), getId() ) );
    }
    componentIds.set( _id );
    final boolean mustPerformCreate = Flags.ALLOCATE == ( _flags & Flags.ALLOCATE );
    final T component = mustPerformCreate ? performCreate( entityId ) : null;
    if ( entity.isNotAdding() )
    {
      for ( final EntityCollection collection : _collections )
      {
        collection.componentChange( entity );
      }
      if ( _world.willPropagateSpyEvents() )
      {
        _world.getSpy().reportSpyEvent( new ComponentAddCompleteEvent( _world, entity.getId(), getId() ) );
      }
    }
    return shouldReturnComponent && !mustPerformCreate ? get( entityId ) : component;
  }

  /**
   * Template method implemented by the ComponentManager implementation to perform component creation.
   *
   * @param entityId the id of the entity.
   */
  @Nonnull
  abstract T performCreate( int entityId );

  /**
   * Remove the component for the specified entity.
   * The component MUST exist for the entity.
   *
   * @param entityId the id of the entity.
   */
  public final void remove( final int entityId )
  {
    ensureCurrentWorldMatches();
    final Entity entity = getEntityById( entityId );
    final BitSet componentIds = entity.getComponentIds();
    if ( Galdr.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> componentIds.get( _id ),
                    () -> "Galdr-0030: The ComponentManager.remove() method for the component named '" + getName() +
                          "' was invoked but the entity " + entityId + " does not have the component." );
    }
    componentIds.clear( _id );
    if ( entity.isNotRemoving() )
    {
      if ( _world.willPropagateSpyEvents() )
      {
        _world.getSpy().reportSpyEvent( new ComponentRemoveStartEvent( _world, entity.getId(), getId() ) );
      }
      for ( final EntityCollection collection : _collections )
      {
        collection.componentChange( entity );
      }
    }
    performRemove( entityId );
  }

  /**
   * Template method implemented by the ComponentManager implementation to perform removal.
   *
   * @param entityId the id of the entity.
   */
  abstract void performRemove( int entityId );

  @OmitSymbol( unless = "galdr.debug_to_string" )
  @Override
  public String toString()
  {
    if ( Galdr.areDebugToStringMethodsEnabled() )
    {
      return "ComponentManager[" + getName() + "=" + _id + "]";
    }
    else
    {
      return super.toString();
    }
  }

  @Nonnull
  List<EntityCollection> getCollections()
  {
    return _collections;
  }

  /**
   * Return the info associated with this class.
   *
   * @return the info associated with this class.
   */
  @SuppressWarnings( "ConstantConditions" )
  @OmitSymbol( unless = "galdr.enable_spies" )
  @Nonnull
  ComponentInfo asInfo()
  {
    if ( Galdr.shouldCheckInvariants() )
    {
      invariant( Galdr::areSpiesEnabled,
                 () -> "Galdr-0040: ComponentManager.asInfo() invoked but Galdr.areSpiesEnabled() returned false." );
    }
    if ( Galdr.areSpiesEnabled() && null == _info )
    {
      _info = new ComponentInfoImpl( this );
    }
    return Galdr.areSpiesEnabled() ? _info : null;
  }

  @Override
  public boolean equals( final Object o )
  {
    return o instanceof ComponentManager &&
           _id == ( (ComponentManager<?>) o )._id &&
           _world == ( (ComponentManager<?>) o )._world;
  }

  @Override
  public int hashCode()
  {
    return _id;
  }

  void addCollection( @Nonnull final EntityCollection collection )
  {
    if ( Galdr.shouldCheckInvariants() )
    {
      invariant( () -> !_collections.contains( collection ),
                 () -> "Galdr-0029: The ComponentManager.addCollection() method for the component named '" +
                       getName() + "' was invoked but collection is already registered with ComponentManager." );
    }
    _collections.add( collection );
  }

  void removeCollection( @Nonnull final EntityCollection collection )
  {
    if ( Galdr.shouldCheckInvariants() )
    {
      invariant( () -> _collections.contains( collection ),
                 () -> "Galdr-0042: The ComponentManager.removeCollection() method for the component named '" +
                       getName() + "' was invoked but collection is not registered with ComponentManager." );
    }
    _collections.remove( collection );
  }

  @OmitSymbol( unless = "galdr.check_api_invariants" )
  private void ensureCurrentWorldMatches()
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      final World activeWorld = World.current();
      final World componentWorld = getWorld();
      apiInvariant( () -> activeWorld == componentWorld,
                    () -> "Galdr-0035: ComponentManager method invoked in the context of the world '" +
                          activeWorld.getName() + "' but the component belongs to the world '" +
                          componentWorld.getName() + "'" );
    }
  }

  static final class Flags
  {
    // Flag indicating whether ComponentManager must explicitly allocate a Component
    // If not present then the component manager assumes that it is pre-allocated nor
    // not allocateable and just accesses component
    static final int ALLOCATE = 1 << 1;

    private Flags()
    {
    }
  }
}
