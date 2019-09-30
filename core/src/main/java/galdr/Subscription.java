package galdr;

import java.util.BitSet;
import javax.annotation.Nonnull;
import static org.realityforge.braincheck.Guards.*;

/**
 * A subscription is a set of entities that match an {@link AreaOfInterest} that 1 or more
 * subscribers are interested in. A single subscriber may iterate over subscription at any
 * one time.
 */
final class Subscription
{
  /**
   * The area of interest against which entities are matched against to determine whether they
   * belong in the subscription.
   */
  @Nonnull
  private final AreaOfInterest _areaOfInterest;
  /**
   * The bitset containing entities that are part of the subscription.
   */
  @Nonnull
  private final BitSet _entities;
  /**
   * The bitset containing entities that were added to the subscription after the
   * iteration started and were below the _currentEntityId and thus must be visited
   * in a second pass. If the entity was added to the subscription after the current
   * pointer then it will be picked up in the standard processing steps.
   */
  @Nonnull
  private final BitSet _newEntities;
  /**
   * The id of the current entity if iterating over entities else -1.
   */
  private int _currentEntityId;
  /**
   * Flags describing current state of Subscription.
   */
  private int _flags;

  Subscription( @Nonnull final AreaOfInterest areaOfInterest, final int initialEntityCount )
  {
    _areaOfInterest = areaOfInterest;
    _entities = new BitSet( initialEntityCount );
    _newEntities = new BitSet( initialEntityCount );
    _currentEntityId = -1;
    _flags = 0;
  }

  //TODO: Pass in subscriber here when invariant checks enabled and verify same subscriber until completed
  int nextEntity()
  {
    ensureNotDisposed();
    if ( isProcessingNewEntities() )
    {
      _newEntities.clear( _currentEntityId );
      _currentEntityId = _newEntities.nextSetBit( _currentEntityId + 1 );
      if ( -1 == _currentEntityId )
      {
        // If we have added more entities to the _newEntities prior to current pointer
        // then start scanning from the start of the list again
        if ( hasNewEntities() )
        {
          _currentEntityId = _newEntities.nextSetBit( _currentEntityId + 1 );
          // By definition if we have set HAS_NEW_ENTITIES we should be able to find one
          // unless of course it was added to AOI and remove from AOI within same processing
          // round in which case this
          if ( -1 == _currentEntityId )
          {
            _flags = ( _flags & ~Flags.PROCESSING_NEW_ENTITIES ) & ~Flags.HAS_NEW_ENTITIES;
          }
        }
        else
        {
          _flags &= ~Flags.PROCESSING_NEW_ENTITIES;
        }
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
          }
        }
      }
    }
    return _currentEntityId;
  }

  void entityAdd( @Nonnull final Entity entity )
  {
    ensureNotDisposed();
    if ( Galdr.shouldCheckInvariants() )
    {
      invariant( entity::isAlive,
                 () -> "Galdr-0022: Invoked Subscription.entityAdded with invalid Entity." );
    }
    if ( _areaOfInterest.matches( entity.getComponentIds() ) )
    {
      addInterestInEntity( entity.getId() );
    }
  }

  void entityRemove( @Nonnull final Entity entity )
  {
    ensureNotDisposed();
    if ( Galdr.shouldCheckInvariants() )
    {
      invariant( entity::isAlive,
                 () -> "Galdr-0018: Invoked Subscription.entityRemoved with invalid Entity." );
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
    if ( Galdr.shouldCheckInvariants() )
    {
      invariant( entity::isAlive,
                 () -> "Galdr-0018: Invoked Subscription.componentChanged with invalid Entity." );
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

  private void ensureNotDisposed()
  {
    if ( Galdr.shouldCheckInvariants() )
    {
      invariant( this::isNotDisposed,
                 () -> "Galdr-0015: Invoked method on Subscription when subscription is disposed." );
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

  private boolean hasNewEntities()
  {
    return Flags.HAS_NEW_ENTITIES == ( _flags & Flags.HAS_NEW_ENTITIES );
  }

  private boolean isProcessingNewEntities()
  {
    return Flags.PROCESSING_NEW_ENTITIES == ( _flags & Flags.PROCESSING_NEW_ENTITIES );
  }

  private boolean isNotDisposed()
  {
    return !isDisposed();
  }

  private boolean isDisposed()
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

  int getFlags()
  {
    return _flags;
  }

  static final class Flags
  {
    /**
     * A flag set when the Subscription is processing entities from the _newEntities set.
     */
    static final int PROCESSING_NEW_ENTITIES = 1 << 1;
    /**
     * A flag set when the Subscription has added entities to the _newEntities set but has yet to start processing them.
     */
    static final int HAS_NEW_ENTITIES = 1 << 2;
    /**
     * The subscription has been disposed and should no longer be intereacted with.
     */
    static final int DISPOSED = 1 << 3;

    private Flags()
    {
    }
  }
}
