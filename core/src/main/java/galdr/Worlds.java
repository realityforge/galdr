package galdr;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A class containing static methods for creating worlds.
 */
public final class Worlds
{
  private Worlds()
  {
  }

  /**
   * Return a builder to create a world.
   *
   * @return the builder object to create a world.
   */
  @Nonnull
  public static WorldBuilder world()
  {
    return world( null );
  }

  /**
   * Return a builder to create a world with the specified name.
   *
   * @param name the human consumable name of the world. MUST be <code>null</code> if {@link Galdr#areNamesEnabled()} returns <code>false</code> otherwise may be non-null.
   * @return the builder object to create a world.
   */
  @Nonnull
  public static WorldBuilder world( @Nullable final String name )
  {
    return new WorldBuilder( name );
  }
}
