package galdr;

import java.util.Objects;
import javax.annotation.Nonnull;

public final class Entity
{
  @Nonnull
  private final World world;
  private final int id;

  Entity( @Nonnull final World world, final int id )
  {
    this.world = Objects.requireNonNull( world );
    this.id = id;
  }

  @Nonnull
  public World getWorld()
  {
    return world;
  }

  public int getId()
  {
    return id;
  }
}
