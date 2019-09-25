package galdr;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

final class Entity
{
  private final int _id;
  private int _flags;
  @Nonnull
  private final BitSet _componentIds;
  /**
   * Links from other entities to this entity.
   * In the future we should optimize this into a doubly linked list where each Link points up and down the tree.
   */
  @Nullable
  private List<Link> _inwardLinks;
  /**
   * Links from this entity to other entities.
   * In the future we should optimize this into a doubly linked list where each Link points up and down the tree.
   */
  @Nullable
  private List<Link> _outwardLinks;

  Entity( final int id, final int componentCount )
  {
    assert id >= 0;
    _id = id;
    _componentIds = new BitSet( componentCount );
  }

  int getId()
  {
    return _id;
  }

  @Nonnull
  BitSet getComponentIds()
  {
    return _componentIds;
  }

  void reset()
  {
    invalidateInwardLinks();
    invalidateOutwardLinks();
    _flags = 0;
    _componentIds.clear();
  }

  private void invalidateInwardLinks()
  {
    if ( null != _inwardLinks )
    {
      for ( final Link link : _inwardLinks )
      {
        link.invalidate( false );
      }
    }
    // TODO: Consider recycling the list if we ever find our applications have a lot of links
    //  and we have yet to move to doubly linked list representation
    _inwardLinks = null;
  }

  private void invalidateOutwardLinks()
  {
    if ( null != _outwardLinks )
    {
      for ( final Link link : _outwardLinks )
      {
        link.invalidate( true );
      }
    }
    // TODO: Consider recycling the list if we ever find our applications have a lot of links
    //  and we have yet to move to doubly linked list representation
    _outwardLinks = null;
  }

  boolean isAlive()
  {
    return isFlag( Flags.ALIVE );
  }

  void setAlive()
  {
    _flags |= Flags.ALIVE;
  }

  void clearAlive()
  {
    _flags &= ~Flags.ALIVE;
  }

  boolean isAdding()
  {
    return isFlag( Flags.ADDING );
  }

  void setAdding()
  {
    _flags |= Flags.ADDING;
  }

  void clearAdding()
  {
    _flags &= ~Flags.ADDING;
  }

  boolean isRemoving()
  {
    return isFlag( Flags.REMOVING );
  }

  void setRemoving()
  {
    _flags |= Flags.REMOVING;
  }

  private boolean isFlag( final int flag )
  {
    return ( _flags & flag ) == flag;
  }

  void linkOutgoing( @Nonnull final Link link )
  {
    if ( null == _outwardLinks )
    {
      _outwardLinks = new ArrayList<>();
    }
    _outwardLinks.add( link );
  }

  void linkIncoming( @Nonnull final Link link )
  {
    if ( null == _inwardLinks )
    {
      _inwardLinks = new ArrayList<>();
    }
    _inwardLinks.add( link );
  }

  void unlinkIncoming( @Nonnull final Link link )
  {
    assert null != _inwardLinks;
    final boolean removed = _inwardLinks.remove( link );
  }

  void unlinkOutgoing( @Nonnull final Link link )
  {
    assert null != _outwardLinks;
    final boolean removed = _outwardLinks.remove( link );
  }

  @Nonnull
  List<Link> getInwardLinks()
  {
    return null == _inwardLinks ? Collections.emptyList() : Collections.unmodifiableList( _inwardLinks );
  }

  @Nonnull
  List<Link> getOutwardLinks()
  {
    return null == _outwardLinks ? Collections.emptyList() : Collections.unmodifiableList( _outwardLinks );
  }

  static final class Flags
  {
    static final int ALIVE = 1 << 1;
    static final int ADDING = 1 << 2;
    static final int REMOVING = 1 << 3;

    private Flags()
    {
    }
  }
}
