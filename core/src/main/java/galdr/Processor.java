package galdr;

import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * Base processor class.
 */
public abstract class Processor
{
  @Nonnull
  private final World _world;

  protected Processor( @Nonnull final World world )
  {
    _world = Objects.requireNonNull( world );
  }

  public abstract void process( float timeDelta );

  @Nonnull
  protected final World getWorld()
  {
    return _world;
  }
}
