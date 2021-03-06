package galdr;

import galdr.spy.CollectionInfo;
import grim.annotations.OmitSymbol;
import java.util.BitSet;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

/**
 * An EntityCollection is a set of entities maintained by the runtime. The entities match an
 * {@link AreaOfInterest}. One or more subscribers are subscribed to the collection. At any one
 * time, a single subscriber may be iterating over the collection.
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
  private Subscription _iteratingSubscription;
  /**
   * A count of how many subscriptions are attached to the collection.
   */
  private int _refCount;
  /**
   * Cached info object associated with element.
   * This should be null if {@link Galdr#areSpiesEnabled()} is false.
   */
  @OmitSymbol( unless = "galdr.enable_spies" )
  @Nullable
  private CollectionInfo _info;

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

  int getRefCount()
  {
    return _refCount;
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

  /**
   * Return the info associated with this class.
   *
   * @return the info associated with this class.
   */
  @SuppressWarnings( "ConstantConditions" )
  @OmitSymbol( unless = "galdr.enable_spies" )
  @Nonnull
  CollectionInfo asInfo()
  {
    if ( Galdr.shouldCheckInvariants() )
    {
      invariant( Galdr::areSpiesEnabled,
                 () -> "Galdr-0040: EntityCollection.asInfo() invoked but Galdr.areSpiesEnabled() returned false." );
    }
    if ( Galdr.areSpiesEnabled() && null == _info )
    {
      _info = new CollectionInfoImpl( this );
    }
    return Galdr.areSpiesEnabled() ? _info : null;
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
      invariant( () -> null == _iteratingSubscription,
                 () -> "Galdr-0022: EntityCollection.beginIteration() invoked with subscription named '" +
                       subscription.getName() + "' but an existing iteration is in progress with subscription named '" +
                       Objects.requireNonNull( _iteratingSubscription ).getName() + "'." );
      _iteratingSubscription = subscription;
    }
  }

  /**
   * This should only be invoked if the subscription needs to cancel an in-progress iteration.
   * Iteration will complete normally when there are no entities to return.
   *
   * @param subscription the subscription that begun the iteration.
   */
  void abortIteration( @Nonnull final Subscription subscription )
  {
    ensureNotDisposed();
    ensureCurrentWorldMatches();
    if ( Galdr.shouldCheckInvariants() )
    {
      invariant( () -> null != _iteratingSubscription,
                 () -> "Galdr-0047: EntityCollection.abortIteration() invoked with subscription named '" +
                       subscription.getName() + "' but no iteration was active." );
      invariant( () -> subscription == _iteratingSubscription,
                 () -> "Galdr-0027: EntityCollection.abortIteration() invoked with subscription named '" +
                       subscription.getName() + "' but this does not match the existing subscription named '" +
                       Objects.requireNonNull( _iteratingSubscription ).getName() + "'." );
    }
    _currentEntityId = -1;
    if ( hasNewEntities() )
    {
      _flags = ( _flags & ~Flags.PROCESSING_NEW_ENTITIES ) & ~Flags.HAS_NEW_ENTITIES;
      _newEntities.clear();
    }
    _iteratingSubscription = null;
  }

  int nextEntity( @Nonnull final Subscription subscription )
  {
    ensureNotDisposed();
    ensureCurrentWorldMatches();
    if ( Galdr.shouldCheckInvariants() )
    {
      invariant( () -> null != _iteratingSubscription,
                 () -> "Galdr-0047: EntityCollection.nextEntity() invoked with subscription named '" +
                       subscription.getName() + "' but no iteration was active." );
      invariant( () -> subscription == _iteratingSubscription,
                 () -> "Galdr-0025: EntityCollection.nextEntity() invoked with subscription named '" +
                       subscription.getName() + "' but an existing iteration is in progress with subscription " +
                       "named '" + Objects.requireNonNull( _iteratingSubscription ).getName() + "'." );
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
      _iteratingSubscription = null;
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

  @OmitSymbol( unless = "galdr.check_invariants" )
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
    return null != getIteratingSubscription();
  }

  @Nonnull
  World getWorld()
  {
    return _world;
  }

  @Nullable
  Object getIteratingSubscription()
  {
    return _iteratingSubscription;
  }

  @OmitSymbol( unless = "galdr.check_api_invariants" )
  void ensureCurrentWorldMatches()
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      final World activeWorld = World.current();
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
     * The EntityCollection has been disposed and should no longer be interacted with.
     */
    static final int DISPOSED = 1 << 3;

    private Flags()
    {
    }
  }
}
