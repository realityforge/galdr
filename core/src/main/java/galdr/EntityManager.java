package galdr;

import galdr.spy.EntityPostAddEvent;
import galdr.spy.EntityPreRemoveEvent;
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
  private int _nextIndex;

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
  boolean isAlive( final int index )
  {
    return index < _nextIndex && !_free.get( index ) && _entities[ index ].isAlive();
  }

  @Nonnull
  Entity createEntity( @Nonnull final BitSet componentIds )
  {
    //TODO: Ensure componentIds are valid
    final Entity entity = allocateEntity();
    createComponents( entity, componentIds );
    entity.setAlive();
    if ( _world.willPropagateSpyEvents() )
    {
      _world.getSpy().reportSpyEvent( new EntityPostAddEvent( _world, entity.getId() ) );
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
      //TODO: Use a different create that does not generate spy/other messages
      registry.getComponentManagerByIndex( current ).create( entityId );
      entity.getComponentIds().set( current );
    }
  }

  @Nonnull
  private Entity allocateEntity()
  {
    final int nextFreeIndex = _free.nextSetBit( 0 );
    if ( -1 != nextFreeIndex )
    {
      final Entity entity = _entities[ nextFreeIndex ];
      // If it has been free-ed then it must have been already allocated
      assert null != entity;
      _free.clear( nextFreeIndex );
      return entity;
    }
    else
    {
      final int index = _nextIndex++;
      final boolean mustGrow = index >= _entities.length;
      assert mustGrow || null == _entities[ index ];
      final Entity entity = new Entity( index, _world.getComponentRegistry().size() );
      if ( mustGrow )
      {
        grow( Math.max( 2 * _entities.length, ( 3 * ( _entities.length + 1 ) ) / 2 ) );
      }
      _entities[ index ] = entity;
      return entity;
    }
  }

  void disposeEntity( final int entityId )
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> entityId <= _entities.length && !_free.get( entityId ) && null != _entities[ entityId ],
                    () -> "Galdr-0009: Attempting to dispose entity " + entityId + " but entity is not allocated." );
    }
    final Entity entity = _entities[ entityId ];
    entity.setRemoving();
    if ( _world.willPropagateSpyEvents() )
    {
      _world.getSpy().reportSpyEvent( new EntityPreRemoveEvent( _world, entity.getId() ) );
    }
    entity.clearAlive();
    removeComponents( entity );
    entity.reset();
    _free.set( entityId );
  }

  private void removeComponents( @Nonnull final Entity entity )
  {
    final ComponentRegistry registry = _world.getComponentRegistry();
    final int entityId = entity.getId();
    final BitSet componentIds = entity.getComponentIds();

    int current = -1;
    while ( -1 != ( current = componentIds.nextSetBit( current + 1 ) ) )
    {
      //TODO: Use a different remove that does not generate spy/other messages
      registry.getComponentManagerByIndex( current ).remove( entityId );
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
