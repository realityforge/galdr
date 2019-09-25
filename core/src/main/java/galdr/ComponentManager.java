package galdr;

import galdr.spy.ComponentAddedEvent;
import galdr.spy.ComponentRemoveStartEvent;
import java.util.BitSet;
import java.util.Objects;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

/**
 * The class responsible for storing components of a particular type.
 */
abstract class ComponentManager<T>
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
   * The java type of the component.
   */
  @Nonnull
  private final Class<T> _type;
  /**
   * Function invoked to create an instance of the component.
   */
  @Nonnull
  private final Supplier<T> _createFn;
  /**
   * The Component API exposed to application code.
   */
  @Nonnull
  private final ComponentAPI<T> _api;

  ComponentManager( @Nonnull final World world,
                    final int id,
                    @Nonnull final Class<T> type,
                    @Nonnull final Supplier<T> createFn )
  {
    _world = Objects.requireNonNull( world );
    _id = id;
    _type = Objects.requireNonNull( type );
    _createFn = Objects.requireNonNull( createFn );
    _api = new ComponentAPI<>( this );
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
   * Return the API for component type.
   *
   * @return the API for the component type.
   */
  @Nonnull
  ComponentAPI<T> getApi()
  {
    return _api;
  }

  /**
   * Return the unique id of the component within the containing {@link World}.
   *
   * @return the unique id of the component within the containing {@link World}.
   */
  int getId()
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
   * Return the function that creates an instance of the component.
   *
   * @return the function that creates an instance of the component.
   */
  @Nonnull
  final Supplier<T> getCreateFn()
  {
    return _createFn;
  }

  /**
   * Return the human readable name of the ComponentManager.
   * This method should NOT be invoked unless {@link Galdr#areNamesEnabled()} returns <code>true</code>.
   *
   * @return the human readable name of the ComponentManager.
   */
  @Nonnull
  String getName()
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      apiInvariant( Galdr::areNamesEnabled,
                    () -> "Galdr-0053: ComponentManager.getName() invoked when Galdr.areNamesEnabled() returns false" );
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
  final boolean has( final int entityId )
  {
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
  final T find( final int entityId )
  {
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
  final T get( final int entityId )
  {
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
  final T findOrCreate( final int entityId )
  {
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
  final T create( final int entityId )
  {
    final Entity entity = getEntityById( entityId );
    final BitSet componentIds = entity.getComponentIds();
    if ( Galdr.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> !componentIds.get( _id ),
                    () -> "Galdr-0031: The ComponentManager.create() method invoked but entity " + entityId +
                          " already has the component named '" + getName() + "'." );
    }
    componentIds.set( _id );
    final T component = performCreate( entityId );
    //TODO: Update AreaOfInterest elements based on component addition
    if ( !entity.isAdding() )
    {
      //TODO: Generate application message for component creation?
      if ( _world.willPropagateSpyEvents() )
      {
        _world.getSpy().reportSpyEvent( new ComponentAddedEvent( _world, entity.getId(), getId() ) );
      }
    }
    return component;
  }

  /**
   * Template method implemented by the ComponentManager implementation to perform component creation.
   *
   * @param entityId the id of the entity.
   */
  abstract T performCreate( int entityId );

  /**
   * Create an instance of the component.
   *
   * @return an instance of the component.
   */
  final T createComponentInstance()
  {
    return getCreateFn().get();
  }

  /**
   * Remove the component for the specified entity.
   * The component MUST exist for the entity.
   *
   * @param entityId the id of the entity.
   */
  final void remove( final int entityId )
  {
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
      //TODO: Generate application message for component removal?
    }
    //TODO: Update AreaOfInterest elements based on component removal
    performRemove( entityId );
  }

  /**
   * Template method implemented by the ComponentManager implementation to perform removal.
   *
   * @param entityId the id of the entity.
   */
  abstract void performRemove( int entityId );

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

  @Override
  public boolean equals( final Object o )
  {
    return o instanceof ComponentManager && _id == ( (ComponentManager) o )._id;
  }

  @Override
  public int hashCode()
  {
    return _id;
  }
}
