package galdr;

import org.realityforge.braincheck.BrainCheckConfig;

/**
 * Provide access to global configuration settings.
 */
public final class Galdr
{
  private Galdr()
  {
  }

  /**
   * Return true if invariants will be checked.
   *
   * @return true if invariants will be checked.
   */
  public static boolean shouldCheckInvariants()
  {
    return GaldrConfig.checkInvariants() && BrainCheckConfig.checkInvariants();
  }

  /**
   * Return true if apiInvariants will be checked.
   *
   * @return true if apiInvariants will be checked.
   */
  public static boolean shouldCheckApiInvariants()
  {
    return GaldrConfig.checkApiInvariants() && BrainCheckConfig.checkApiInvariants();
  }
}
