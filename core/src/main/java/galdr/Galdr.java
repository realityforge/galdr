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
   * Return true if elements are expected to be named.
   * Names are primarily used for debugging and runtime inspection.
   *
   * @return true if elements are expected to be named.
   */
  public static boolean areNamesEnabled()
  {
    return GaldrConfig.areNamesEnabled();
  }

  /**
   * Return true if toString() methods should produce useful debug output, false otherwise.
   * Removing debug output from toString() methods will significantly reduce the code size when compiled to javascript.
   *
   * @return true if toString() methods should produce useful debug output, false otherwise.
   */
  public static boolean areDebugToStringMethodsEnabled()
  {
    return GaldrConfig.areDebugToStringMethodsEnabled();
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
