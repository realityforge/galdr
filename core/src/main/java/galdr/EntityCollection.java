package galdr;

import java.util.BitSet;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

/**
 * An EntityCollection is a set of entities that match an {@link AreaOfInterest} that 1 or more
 * subscribers are interested in. A single subscriber may iterate over collection at any
 * one time.
 */
final class EntityCollection
{
  /**
   * The world containing the collection.
   */
  @Nonnull
  private final World _world;
  /**
   * The area of interest against which entities are matched against to determine whether they
   * belong in the collection.
   */
  @Nonnull
  private final AreaOfInterest _areaOfInterest;
  /**
   * The bitset containing entities that are part of the collection.
   */
  @Nonnull
  private final BitSet _entities;
  /**
   * The bitset containing entities that were added to the collection after the
   * iteration started and have an entityId less than the _currentEntityId and thus must be visited
   * in a second pass. If the entity was added to the collection and has an entityId after _currentEntityId
   * then it need not be added to this list as it will be picked up as part of first pass through collection.
   */
  @Nonnull
  private final BitSet _newEntities;
  /**
   * The id of the current entity if iterating over entities else -1.
   */
  private int _currentEntityId;
  /**
   * Flags describing current state of the collection.
   */
  private int _flags;
  /**
   * The subscription that is currently iterating over the collection.
   */
  @Nullable
  private Subscription _subscription;
  /**
   * A count of how many subscriptions are attached to the collection.
   */
  private int _refCount;

  EntityCollection( @Nonnull final World world,
                    @Nonnull final AreaOfInterest areaOfInterest,
                    final int initialEntityCount )
  {
    _world = Objects.requireNonNull( world );
    _areaOfInterest = Objects.requireNonNull( areaOfInterest );
    _entities = new BitSet( initialEntityCount );
    _newEntities = new BitSet( initialEntityCount );
    _currentEntityId = -1;
    _flags = 0;
  }

  void incRef()
  {
    ensureCurrentWorldMatches();
    _refCount++;
  }

  void decRef()
  {
    ensureCurrentWorldMatches();
    assert _refCount > 0;
    _refCount--;
    if ( 0 == _refCount )
    {
      _world.removeCollection( this );
    }
  }

  void beginIteration( @Nonnull final Subscription subscription )
  {
    ensureNotDisposed();
    ensureCurrentWorldMatches();
    if ( Galdr.shouldCheckInvariants() )
    {
      invariant( () -> -1 == _currentEntityId,
                 () -> "Galdr-0032: EntityCollection.beginIteration() invoked when _currentEntityId " +
                       "has not been reset. Current value " + _currentEntityId );
      invariant( () -> null == _subscription,
                 () -> "Galdr-0022: EntityCollection.beginIteration() invoked with subscription named '" +
                       subscription.getName() + "' but an existing iteration is in progress with subscription named '" +
                       Objects.requireNonNull( _subscription ).getName() + "'." );
      _subscription = subscription;
    }
  }

  /**
   * This should only be invoked if the owner wants to cancel an in progress iteration.
   * Normal iteration will complete out when no entities to return.
   *
   * @param subscription the current owner.
   */
  void abortIteration( @Nonnull final Subscription subscription )
  {
    ensureNotDisposed();
    ensureCurrentWorldMatches();
    if ( Galdr.shouldCheckInvariants() )
    {
      invariant( () -> null != _subscription,
                 () -> "Galdr-0047: EntityCollection.abortIteration() invoked with subscription named '" +
                       subscription.getName() + "' but no iteration was active." );
      invariant( () -> subscription == _subscription,
                 () -> "Galdr-0027: EntityCollection.abortIteration() invoked with subscription named '" +
                       subscription.getName() + "' but this does not match the existing subscription named '" +
                       Objects.requireNonNull( _subscription ).getName() + "'." );
    }
    _currentEntityId = -1;
    if ( hasNewEntities() )
    {
      _flags = ( _flags & ~Flags.PROCESSING_NEW_ENTITIES ) & ~Flags.HAS_NEW_ENTITIES;
      _newEntities.clear();
    }
    _subscription = null;
  }

  int nextEntity( @Nonnull final Subscription subscription )
  {
    ensureNotDisposed();
    ensureCurrentWorldMatches();
    if ( Galdr.shouldCheckInvariants() )
    {
      invariant( () -> null != _subscription,
                 () -> "Galdr-0047: EntityCollection.nextEntity() invoked with subscription named '" +
                       subscription.getName() + "' but no iteration was active." );
      invariant( () -> subscription == _subscription,
                 () -> "Galdr-0025: EntityCollection.nextEntity() invoked with subscription named '" +
                       subscription.getName() + "' but an existing iteration is in progress with subscription " +
                       "named '" + Objects.requireNonNull( _subscription ).getName() + "'." );
    }
    if ( isProcessingNewEntities() )
    {
      _currentEntityId = _newEntities.nextSetBit( _currentEntityId + 1 );
      if ( -1 == _currentEntityId )
      {
        assert hasNewEntities();
        // If we have added more entities to the _newEntities prior to current pointer
        // then start scanning from the start of the list again
        _currentEntityId = _newEntities.nextSetBit( _currentEntityId + 1 );
        // By definition if we have set HAS_NEW_ENTITIES we should be able to find one
        // unless of course it was added to AOI and removed from AOI before we processed
        // the entity
        if ( -1 == _currentEntityId )
        {
          _flags = ( _flags & ~Flags.PROCESSING_NEW_ENTITIES ) & ~Flags.HAS_NEW_ENTITIES;
        }
        else
        {
          _newEntities.clear( _currentEntityId );
        }
      }
      else
      {
        _newEntities.clear( _currentEntityId );
      }
    }
    else
    {
      _currentEntityId = _entities.nextSetBit( _currentEntityId + 1 );
      if ( -1 == _currentEntityId )
      {
        if ( hasNewEntities() )
        {
          // Start processing from the _newEntities list
          _currentEntityId = _newEntities.nextSetBit( 0 );
          if ( -1 != _currentEntityId )
          {
            _flags |= Flags.PROCESSING_NEW_ENTITIES;
            _newEntities.clear( _currentEntityId );
          }
        }
      }
    }
    if ( -1 == _currentEntityId )
    {
      _subscription = null;
    }
    return _currentEntityId;
  }

  void entityAdd( @Nonnull final Entity entity )
  {
    ensureNotDisposed();
    ensureCurrentWorldMatches();
    if ( Galdr.shouldCheckInvariants() )
    {
      invariant( entity::isAlive,
                 () -> "Galdr-0018: Invoked EntityCollection.entityAdd with invalid Entity." );
    }
    if ( _areaOfInterest.matches( entity.getComponentIds() ) )
    {
      addInterestInEntity( entity.getId() );
    }
  }

  void entityRemove( @Nonnull final Entity entity )
  {
    ensureNotDisposed();
    ensureCurrentWorldMatches();
    if ( Galdr.shouldCheckInvariants() )
    {
      invariant( entity::isAlive,
                 () -> "Galdr-0018: Invoked EntityCollection.entityRemove with invalid Entity." );
    }
    final int entityId = entity.getId();
    // We check whether the entity is in the list as it is slightly faster to check than calling
    // removeInterestInEntity when the entity is not in the list even if it is slightly faster
    // when the entity is present.
    // TODO: Use custom BitSet implementation that implements a "boolean clearIfSet()" method
    //  so we can avoid invoking _newEntities.clear( entityId ) unless necessary.
    if ( _entities.get( entityId ) )
    {
      removeInterestInEntity( entityId );
    }
  }

  void componentChange( @Nonnull final Entity entity )
  {
    ensureNotDisposed();
    ensureCurrentWorldMatches();
    if ( Galdr.shouldCheckInvariants() )
    {
      invariant( entity::isAlive,
                 () -> "Galdr-0018: Invoked EntityCollection.componentChange with invalid Entity." );
    }
    final int entityId = entity.getId();
    if ( _areaOfInterest.matches( entity.getComponentIds() ) )
    {
      addInterestInEntity( entity.getId() );
    }
    else
    {
      removeInterestInEntity( entityId );
    }
  }

  void ensureNotDisposed()
  {
    if ( Galdr.shouldCheckInvariants() )
    {
      invariant( this::isNotDisposed,
                 () -> "Galdr-0015: Invoked method on a disposed EntityCollection." );
    }
  }

  private void addInterestInEntity( final int entityId )
  {
    _entities.set( entityId );
    final boolean isProcessingNewEntities = isProcessingNewEntities();
    final boolean beforeCurrentIndex = _currentEntityId > entityId;

    if ( beforeCurrentIndex || isProcessingNewEntities )
    {
      if ( beforeCurrentIndex )
      {
        _flags |= Flags.HAS_NEW_ENTITIES;
      }
      _newEntities.set( entityId );
    }
  }

  private void removeInterestInEntity( final int entityId )
  {
    _entities.clear( entityId );
    _newEntities.clear( entityId );
  }

  boolean hasNewEntities()
  {
    return Flags.HAS_NEW_ENTITIES == ( _flags & Flags.HAS_NEW_ENTITIES );
  }

  boolean isProcessingNewEntities()
  {
    return Flags.PROCESSING_NEW_ENTITIES == ( _flags & Flags.PROCESSING_NEW_ENTITIES );
  }

  boolean isNotDisposed()
  {
    return !isDisposed();
  }

  boolean isDisposed()
  {
    return Flags.DISPOSED == ( _flags & Flags.DISPOSED );
  }

  void markAsDisposed()
  {
    _flags |= Flags.DISPOSED;
  }

  @Nonnull
  AreaOfInterest getAreaOfInterest()
  {
    return _areaOfInterest;
  }

  @Nonnull
  BitSet getEntities()
  {
    return _entities;
  }

  @Nonnull
  BitSet getNewEntities()
  {
    return _newEntities;
  }

  int getCurrentEntityId()
  {
    return _currentEntityId;
  }

  boolean isIterationInProgress()
  {
    return null != getSubscription();
  }

  @Nonnull
  World getWorld()
  {
    return _world;
  }

  @Nullable
  Object getSubscription()
  {
    return _subscription;
  }

  void ensureCurrentWorldMatches()
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      final World activeWorld = WorldHolder.world();
      apiInvariant( () -> activeWorld == _world,
                    () -> "Galdr-0036: EntityCollection method invoked in the context of the world '" +
                          activeWorld.getName() + "' but the collection belongs to the world '" +
                          _world.getName() + "'" );
    }
  }

  static final class Flags
  {
    /**
     * A flag set when the EntityCollection is processing entities from the _newEntities set.
     */
    static final int PROCESSING_NEW_ENTITIES = 1 << 1;
    /**
     * A flag set when the EntityCollection has added entities to the _newEntities set but has yet to start processing them.
     */
    static final int HAS_NEW_ENTITIES = 1 << 2;
    /**
     * The EntityCollection has been disposed and should no longer be intereacted with.
     */
    static final int DISPOSED = 1 << 3;

    private Flags()
    {
    }
  }
}
