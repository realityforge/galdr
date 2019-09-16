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
   * Return true if multiple worlds are enabled, false otherwise.
   *
   * @return true if multiple worlds are enabled, false otherwise.
   */
  public static boolean areMultipleWorldsEnabled()
  {
    return GaldrConfig.areMultipleWorldsEnabled();
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
   * Return true if arrays passed when constructing system components should be copied.
   * In normal operation, the arrays are expected to be "owned" by the target component and thus need not
   * copied. HOwever in development mode we copy for defensive purposes.
   *
   * @return true if arrays passed when constructing system components should be copied.
   */
  public static boolean shouldCopyArraysPassedToConstructors()
  {
    return GaldrConfig.shouldCopyArraysPassedToConstructors();
  }

  /**
   * Return true if toString() methods should produce useful debug output, false otherwise.
   * Removing debug output from toString() methods will significantly reduce the code size when compiled to javascript.
   *
   * @return true if toString() methods should produce useful debug output, false otherwise.
   */
  public static boolean areDebugToStringMethodsEnabled()
  {
    // Many of the toString() implementations use names so we need to disable
    // debug toString implementation if names disabled
    return areNamesEnabled() && GaldrConfig.areDebugToStringMethodsEnabled();
  }

  /**
   * Return true if error handlers are enabled.
   *
   * @return true if error handlers are enabled, false otherwise.
   */
  public static boolean areErrorHandlersEnabled()
  {
    return GaldrConfig.areErrorHandlersEnabled();
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
