package galdr;

import galdr.spy.LinkRemoveCompleteEvent;
import galdr.spy.LinkRemoveStartEvent;
import grim.annotations.OmitSymbol;
import grim.annotations.OmitType;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

public final class Link
{
  @Nullable
  private Entity _sourceEntity;
  @Nullable
  private Entity _targetEntity;
  // Consider collapsing these booleans into flags
  private final boolean _cascadeSourceRemoveToTarget;
  private final boolean _cascadeTargetRemoveToSource;

  Link( @Nonnull final Entity sourceEntity,
        @Nonnull final Entity targetEntity,
        final boolean cascadeSourceRemoveToTarget,
        final boolean cascadeTargetRemoveToSource )
  {
    _sourceEntity = Objects.requireNonNull( sourceEntity );
    _targetEntity = Objects.requireNonNull( targetEntity );
    _cascadeSourceRemoveToTarget = cascadeSourceRemoveToTarget;
    _cascadeTargetRemoveToSource = cascadeTargetRemoveToSource;
    _sourceEntity.linkOutgoing( this );
    _targetEntity.linkIncoming( this );
  }

  /**
   * Return the id of the target entity.
   *
   * @return the id of the target entity.
   */
  public final int getId()
  {
    return getTargetEntity().getId();
  }

  /**
   * Return true if the Link is still valid, false otherwise.
   * A Link becomes invalid if {@link #dispose()} is invoked or one of the associated entities is removed.
   *
   * @return true if the Link is still valid, false otherwise.
   */
  public final boolean isValid()
  {
    return null != _targetEntity;
  }

  @Nonnull
  final Entity getSourceEntity()
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      apiInvariant( this::isValid, () -> "Galdr-0007: The Link.getSourceEntity() method invoked on invalid link." );
    }
    assert null != _sourceEntity;
    return _sourceEntity;
  }

  @Nonnull
  final Entity getTargetEntity()
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      apiInvariant( this::isValid, () -> "Galdr-0016: The Link.getTargetEntity() method invoked on invalid link." );
    }
    assert null != _targetEntity;
    return _targetEntity;
  }

  boolean shouldCascadeSourceRemoveToTarget()
  {
    return _cascadeSourceRemoveToTarget;
  }

  boolean shouldCascadeTargetRemoveToSource()
  {
    return _cascadeTargetRemoveToSource;
  }

  /**
   * Mark link as invalid.
   */
  void invalidate( final boolean sourceRemove )
  {
    // The use of WorldHolder.world() in this scenario seems wrong. Is there a better way?
    // We do not want to add World instance to Link as that will significantly increase memory
    // pressure and World is not cached on related Entity instances so maybe WorldHolder is the only way.

    if ( Galdr.shouldCheckInvariants() )
    {
      invariant( this::isValid, () -> "Galdr-0017: Link.invalidate() method invoked on invalid link." );
    }
    assert null != _sourceEntity;
    assert null != _targetEntity;
    reportLinkRemoveStartEvent( _sourceEntity.getId(), _targetEntity.getId() );
    final int sourceEntityId = _sourceEntity.getId();
    final int targetEntityId = _targetEntity.getId();
    if ( sourceRemove )
    {
      if ( _targetEntity.isNotRemoving() )
      {
        _targetEntity.unlinkIncoming( this );
      }
      if ( shouldCascadeSourceRemoveToTarget() )
      {
        WorldHolder.world().disposeEntity( targetEntityId );
      }
    }
    else
    {
      if ( _sourceEntity.isNotRemoving() )
      {
        _sourceEntity.unlinkOutgoing( this );
      }
      if ( shouldCascadeTargetRemoveToSource() )
      {
        WorldHolder.world().disposeEntity( sourceEntityId );
      }
    }
    _sourceEntity = null;
    _targetEntity = null;
    reportLinkRemoveCompleteEvent( sourceEntityId, targetEntityId );
  }

  void dispose()
  {
    if ( Galdr.shouldCheckInvariants() )
    {
      invariant( this::isValid, () -> "Galdr-0117: Link.dispose() method invoked on invalid link." );
    }
    assert null != _sourceEntity;
    assert null != _targetEntity;
    final int sourceEntityId = _sourceEntity.getId();
    final int targetEntityId = _targetEntity.getId();
    reportLinkRemoveStartEvent( sourceEntityId, targetEntityId );
    _sourceEntity.unlinkOutgoing( this );
    _targetEntity.unlinkIncoming( this );
    _sourceEntity = null;
    _targetEntity = null;
    reportLinkRemoveCompleteEvent( sourceEntityId, targetEntityId );
  }

  @OmitSymbol( unless = "galdr.debug_to_string" )
  @Override
  public final String toString()
  {
    if ( Galdr.areDebugToStringMethodsEnabled() )
    {
      final String desc =
        isValid() ?
        Objects.requireNonNull( _sourceEntity ).getId() + "->" + Objects.requireNonNull( _targetEntity ).getId() :
        "(disposed)";
      return "Link[" + desc + "]";
    }
    else
    {
      return super.toString();
    }
  }

  private void reportLinkRemoveCompleteEvent( final int sourceEntityId, final int targetEntityId )
  {
    final World world = World.current();
    if ( world.willPropagateSpyEvents() )
    {
      world.getSpy().reportSpyEvent( new LinkRemoveCompleteEvent( world, sourceEntityId, targetEntityId ) );
    }
  }

  private void reportLinkRemoveStartEvent( final int sourceEntityId, final int targetEntityId )
  {
    assert null != _sourceEntity;
    assert null != _targetEntity;
    final World world = World.current();
    if ( world.willPropagateSpyEvents() )
    {
      world.getSpy().reportSpyEvent( new LinkRemoveStartEvent( world, sourceEntityId, targetEntityId ) );
    }
  }
}
