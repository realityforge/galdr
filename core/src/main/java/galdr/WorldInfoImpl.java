package galdr;

import galdr.spy.WorldInfo;
import java.util.Objects;
import javax.annotation.Nonnull;

final class WorldInfoImpl
  implements WorldInfo
{
  @Nonnull
  private final World _world;

  WorldInfoImpl( @Nonnull final World world )
  {
    _world = Objects.requireNonNull( world );
  }

  @Nonnull
  @Override
  public String getName()
  {
    return _world.getName();
  }

  @Override
  public int getEntityCount()
  {
    return _world.getEntityManager().getEntityCount();
  }

  @Override
  public int getEntityCapacity()
  {
    return _world.getEntityManager().capacity();
  }

  @Override
  public int getComponentCount()
  {
    return _world.getComponentCount();
  }

  @Override
  public String toString()
  {
    return _world.toString();
  }

  @Override
  public boolean equals( final Object o )
  {
    if ( o instanceof WorldInfoImpl )
    {
      final WorldInfoImpl that = (WorldInfoImpl) o;
      return _world.equals( that._world );
    }
    else
    {
      return false;
    }
  }

  @Override
  public int hashCode()
  {
    return _world.hashCode();
  }
}
