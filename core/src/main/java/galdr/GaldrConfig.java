package galdr;

import javax.annotation.Nonnull;

/**
 * The location of all compile time configuration settings for framework.
 */
final class GaldrConfig
{
  private static final ConfigProvider PROVIDER = new ConfigProvider();
  private static final boolean PRODUCTION_MODE = PROVIDER.isProductionMode();
  private static boolean ENABLE_NAMES = PROVIDER.areNamesEnabled();
  private static boolean COPY_ARRAYS_PASSED_TO_CONSTRUCTORS = PROVIDER.shouldCopyArraysPassedToConstructors();
  private static boolean DEBUG_TO_STRING = PROVIDER.areDebugToStringMethodsEnabled();
  private static boolean CHECK_INVARIANTS = PROVIDER.checkInvariants();
  private static boolean CHECK_API_INVARIANTS = PROVIDER.checkApiInvariants();
  @Nonnull
  private static final String LOGGER_TYPE = PROVIDER.loggerType();

  private GaldrConfig()
  {
  }

  static boolean isDevelopmentMode()
  {
    return !isProductionMode();
  }

  static boolean isProductionMode()
  {
    return PRODUCTION_MODE;
  }

  static boolean areNamesEnabled()
  {
    return ENABLE_NAMES;
  }

  static boolean shouldCopyArraysPassedToConstructors()
  {
    return COPY_ARRAYS_PASSED_TO_CONSTRUCTORS;
  }

  static boolean areDebugToStringMethodsEnabled()
  {
    return DEBUG_TO_STRING;
  }

  static boolean checkInvariants()
  {
    return CHECK_INVARIANTS;
  }

  static boolean checkApiInvariants()
  {
    return CHECK_API_INVARIANTS;
  }

  @Nonnull
  static String loggerType()
  {
    return LOGGER_TYPE;
  }

  private static final class ConfigProvider
    extends AbstractConfigProvider
  {
    @GwtIncompatible
    @Override
    boolean isProductionMode()
    {
      return "production".equals( System.getProperty( "galdr.environment", "production" ) );
    }

    @GwtIncompatible
    @Override
    boolean areNamesEnabled()
    {
      return "true".equals( System.getProperty( "galdr.enable_names", isProductionMode() ? "false" : "true" ) );
    }

    @GwtIncompatible
    @Override
    boolean shouldCopyArraysPassedToConstructors()
    {
      return "true".equals( System.getProperty( "galdr.copy_arrays_passed_to_constructors",
                                                PRODUCTION_MODE ? "false" : "true" ) );
    }

    @GwtIncompatible
    @Override
    boolean areDebugToStringMethodsEnabled()
    {
      return "true".equals( System.getProperty( "galdr.debug_to_string", PRODUCTION_MODE ? "false" : "true" ) );
    }

    @GwtIncompatible
    @Override
    boolean checkInvariants()
    {
      return "true".equals( System.getProperty( "galdr.check_invariants", PRODUCTION_MODE ? "false" : "true" ) );
    }

    @GwtIncompatible
    @Override
    boolean checkApiInvariants()
    {
      return "true".equals( System.getProperty( "galdr.check_api_invariants", PRODUCTION_MODE ? "false" : "true" ) );
    }

    @GwtIncompatible
    @Override
    @Nonnull
    String loggerType()
    {
      return System.getProperty( "galdr.logger", PRODUCTION_MODE ? "basic" : "proxy" );
    }
  }

  @SuppressWarnings( { "unused", "StringEquality" } )
  private static abstract class AbstractConfigProvider
  {
    boolean isProductionMode()
    {
      return "production" == System.getProperty( "galdr.environment" );
    }

    boolean areNamesEnabled()
    {
      return "true" == System.getProperty( "galdr.enable_names" );
    }

    boolean shouldCopyArraysPassedToConstructors()
    {
      return "true" == System.getProperty( "galdr.copy_arrays_passed_to_constructors" );
    }

    boolean areDebugToStringMethodsEnabled()
    {
      return "true" == System.getProperty( "galdr.debug_to_string" );
    }

    boolean checkInvariants()
    {
      return "true" == System.getProperty( "galdr.check_invariants" );
    }

    boolean checkApiInvariants()
    {
      return "true" == System.getProperty( "galdr.check_api_invariants" );
    }

    @Nonnull
    String loggerType()
    {
      /*
       * Valid values are: "none", "basic", "jul" (java.util.logging) and "proxy" (for testing)
       */
      return System.getProperty( "galdr.logger" );
    }
  }
}
