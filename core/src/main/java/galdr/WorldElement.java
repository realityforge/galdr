package galdr;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The base class for elements within the system.
 */
public abstract class WorldElement
  extends Element
{
  WorldElement()
  {
  }

  WorldElement( @Nullable final String name )
  {
    super( name );
  }

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
}
