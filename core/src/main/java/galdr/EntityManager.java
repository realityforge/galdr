package galdr;

import galdr.spy.EntityAddedEvent;
import galdr.spy.EntityWillRemoveEvent;
import java.util.BitSet;
import java.util.Objects;
import javax.annotation.Nonnull;
import static org.realityforge.braincheck.Guards.*;

final class EntityManager
{
  @Nonnull
  private final World _world;
  @Nonnull
  private final BitSet _free = new BitSet();
  @Nonnull
  private Entity[] _entities;
  private int _nextEntityId;

  EntityManager( @Nonnull final World world, final int entityCount )
  {
    assert entityCount > 0;
    _world = Objects.requireNonNull( world );
    _entities = new Entity[ entityCount ];
  }

  /**
   * Return true if the entity is "alive".
   * Alive indicates that they are allocated and have completed creation but have not yet started removal.
   *
   * @return true if the entity is "alive".
   */
  boolean isAlive( final int entityId )
  {
    return entityId < _nextEntityId && !_free.get( entityId ) && _entities[ entityId ].isAlive();
  }

  @Nonnull
  Entity getEntityById( final int entityId )
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> isAllocated( entityId ),
                    () -> "Galdr-0079: Attempting to get entity " + entityId + " but entity is not allocated." );
      apiInvariant( () -> _entities[ entityId ].isAlive(),
                    () -> "Galdr-0078: Attempting to get entity " + entityId +
                          " but entity is allocated but not alive." );
    }
    return _entities[ entityId ];
  }

  @Nonnull
  Entity createEntity( @Nonnull final BitSet componentIds )
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      final ComponentRegistry registry = _world.getComponentRegistry();
      int current = -1;
      while ( -1 != ( current = componentIds.nextSetBit( current + 1 ) ) )
      {
        final int id = current;
        apiInvariant( () -> registry.isComponentIdValid( id ),
                      () -> "Galdr-0006: Attempting to create entity with invalid componentId " + id );
      }
    }
    final Entity entity = allocateEntity();
    entity.setAlive();
    entity.setAdding();
    createComponents( entity, componentIds );
    entity.clearAdding();
    if ( _world.willPropagateSpyEvents() )
    {
      _world.getSpy().reportSpyEvent( new EntityAddedEvent( _world, entity.getId() ) );
    }
    return entity;
  }

  private void createComponents( @Nonnull final Entity entity, @Nonnull final BitSet componentIds )
  {
    final ComponentRegistry registry = _world.getComponentRegistry();
    final int entityId = entity.getId();

    int current = -1;
    while ( -1 != ( current = componentIds.nextSetBit( current + 1 ) ) )
    {
      registry.getComponentManagerById( current ).create( entityId );
    }
  }

  @Nonnull
  private Entity allocateEntity()
  {
    final int nextFreeEntityId = _free.nextSetBit( 0 );
    if ( -1 != nextFreeEntityId )
    {
      final Entity entity = _entities[ nextFreeEntityId ];
      // If it has been free-ed then it must have been already allocated
      assert null != entity;
      _free.clear( nextFreeEntityId );
      return entity;
    }
    else
    {
      final int entityId = _nextEntityId++;
      final boolean mustGrow = entityId >= _entities.length;
      assert mustGrow || null == _entities[ entityId ];
      final Entity entity = new Entity( entityId, _world.getComponentRegistry().size() );
      if ( mustGrow )
      {
        grow( Math.max( 2 * _entities.length, ( 3 * ( _entities.length + 1 ) ) / 2 ) );
      }
      _entities[ entityId ] = entity;
      return entity;
    }
  }

  void disposeEntity( final int entityId )
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> isAllocated( entityId ),
                    () -> "Galdr-0009: Attempting to dispose entity " + entityId + " but entity is not allocated." );
    }
    doDisposeEntity( _entities[ entityId ] );
  }

  void disposeEntity( @Nonnull final Entity entity )
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> isAllocated( entity.getId() ),
                    () -> "Galdr-0009: Attempting to dispose entity " + entity.getId() + " but entity " +
                          "is not allocated." );
    }
    if ( Galdr.shouldCheckInvariants() )
    {
      apiInvariant( () -> _entities[ entity.getId() ] == entity,
                    () -> "Galdr-0019: Attempting to dispose entity " + entity.getId() + " in world '" +
                          _world.getName() + "' but entity was created in a different world." );
    }
    doDisposeEntity( entity );
  }

  private void doDisposeEntity( @Nonnull final Entity entity )
  {
    if ( Galdr.shouldCheckInvariants() )
    {
      invariant( entity::isAlive,
                 () -> "Galdr-0059: Attempting to dispose entity " + entity.getId() +
                       " and entity is allocated but not alive." );
      final World currentWorld = WorldHolder.world();
      invariant( () -> currentWorld == _world,
                 () -> "Galdr-0159: Attempting to dispose entity " + entity.getId() + " which is not contained " +
                       "by the active world." );
    }
    entity.setRemoving();
    if ( _world.willPropagateSpyEvents() )
    {
      _world.getSpy().reportSpyEvent( new EntityWillRemoveEvent( _world, entity.getId() ) );
    }
    removeComponents( entity );
    entity.reset();
    _free.set( entity.getId() );
  }

  /**
   * Return true if the specified entityId has been allocated.
   *
   * @param entityId the entity id.
   * @return true if the specified entityId has been allocated.
   */
  private boolean isAllocated( final int entityId )
  {
    return entityId >= 0 && entityId <= _entities.length && !_free.get( entityId ) && null != _entities[ entityId ];
  }

  private void removeComponents( @Nonnull final Entity entity )
  {
    final ComponentRegistry registry = _world.getComponentRegistry();
    final int entityId = entity.getId();
    final BitSet componentIds = entity.getComponentIds();

    int current = -1;
    while ( -1 != ( current = componentIds.nextSetBit( current + 1 ) ) )
    {
      registry.getComponentManagerById( current ).remove( entityId );
    }
  }

  int capacity()
  {
    return _entities.length;
  }

  private void grow( final int newCapacity )
  {
    final Entity[] oldData = _entities;
    _entities = new Entity[ newCapacity ];
    System.arraycopy( oldData, 0, _entities, 0, oldData.length );
  }
}
