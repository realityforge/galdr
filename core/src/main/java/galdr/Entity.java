package galdr;

import java.util.BitSet;
import javax.annotation.Nonnull;

final class Entity
{
  private final int _id;
  private int _flags;
  @Nonnull
  private final BitSet _componentIds;

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
    _flags = 0;
    _componentIds.clear();
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

  static final class Flags
  {
    static final int ALIVE = 1 << 1;
    static final int REMOVING = 1 << 3;

    private Flags()
    {
    }
  }
}
