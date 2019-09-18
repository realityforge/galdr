package galdr;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

/**
 * A utility class that contains reference to the active world.
 * This is extracted to a separate class to eliminate the <clinit> from {@link Galdr} and thus
 * make it much easier for GWT to optimize out code based on build time compilation parameters.
 */
final class WorldHolder
{
  /**
   * The current active world.
   */
  @Nullable
  private static World c_world;

  private WorldHolder()
  {
  }

  /**
   * Return the current world.
   * It is expected that this method is only invoked when a world is active.
   *
   * @return the World.
   */
  @Nonnull
  static World world()
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> null != c_world, () -> "Galdr-0026: Invoked WorldHolder.world() when no world was active." );
    }
    assert null != c_world;
    return c_world;
  }

  /**
   * Return true if there is a world that has been marked as active.
   *
   * @return true if there is a world that has been marked as active.
   */
  static boolean isActive()
  {
    return null != c_world;
  }

  /**
   * Mark the specified world as active.
   * No other world may be active when this is invoked.
   */
  @SuppressWarnings( "ConstantConditions" )
  static void activateWorld( @Nonnull final World world )
  {
    if ( Galdr.shouldCheckInvariants() )
    {
      invariant( () -> null != world, () -> "Galdr-0023: Invoked WorldHolder.activateWorld() with null world." );
      invariant( () -> null == c_world,
                 () -> "Galdr-0024: Invoked WorldHolder.activateWorld() with world named '" + world.getName() +
                       "' but an existing world named '" + c_world.getName() + "' is active." );
    }
    assert null != world;
    c_world = world;
  }

  /**
   * Deactivate the world.
   * It is expected that the supplied world is the current active world.
   */
  static void deactivateWorld( @Nonnull final World world )
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> c_world == world, () -> "Galdr-0028: Attempted to deactivate world named '" +
                                                  world.getName() + "' that is not active." );
    }
    c_world = null;
  }
}
