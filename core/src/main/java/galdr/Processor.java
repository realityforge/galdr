package galdr;

import javax.annotation.Nonnull;

/**
 * Base processor class.
 */
public abstract class Processor
  extends Element
{
  protected abstract void process( int delta );

  /**
   * Return the world that the element is associated with.
   *
   * @return the associated World.
   */
  @Nonnull
  protected final World world()
  {
    return WorldHolder.world();
  }

  @Nonnull
  @Override
  protected final String getBaseTypeName()
  {
    return "Processor";
  }
}
