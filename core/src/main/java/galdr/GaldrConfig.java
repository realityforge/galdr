package galdr;

import grim.annotations.OmitType;
import javax.annotation.Nonnull;

/**
 * The location of all compile time configuration settings for framework.
 */
@OmitType
final class GaldrConfig
{
  private static final ConfigProvider PROVIDER = new ConfigProvider();
  private static final boolean PRODUCTION_MODE = PROVIDER.isProductionMode();
  private static boolean ENABLE_NAMES = PROVIDER.areNamesEnabled();
  private static boolean ENABLE_SPIES = PROVIDER.areSpiesEnabled();
  private static boolean DEBUG_TO_STRING = PROVIDER.areDebugToStringMethodsEnabled();
  private static boolean ENABLE_ERROR_HANDLERS = PROVIDER.areErrorHandlersEnabled();
  private static boolean ENFORCE_UNMODIFIABLE_COLLECTIONS = PROVIDER.enforceUnmodifiableCollections();
  private static boolean CHECK_INVARIANTS = PROVIDER.checkInvariants();
  private static boolean CHECK_API_INVARIANTS = PROVIDER.checkApiInvariants();
  @Nonnull
  private static final String LOGGER_TYPE = PROVIDER.loggerType();

  private GaldrConfig()
  {
  }

  static boolean isProductionMode()
  {
    return PRODUCTION_MODE;
  }

  static boolean areNamesEnabled()
  {
    return ENABLE_NAMES;
  }

  static boolean areSpiesEnabled()
  {
    return ENABLE_SPIES;
  }

  static boolean areDebugToStringMethodsEnabled()
  {
    return DEBUG_TO_STRING;
  }

  static boolean areErrorHandlersEnabled()
  {
    return ENABLE_ERROR_HANDLERS;
  }

  static boolean enforceUnmodifiableCollections()
  {
    return ENFORCE_UNMODIFIABLE_COLLECTIONS;
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
    boolean areSpiesEnabled()
    {
      return "true".equals( System.getProperty( "galdr.enable_spies", isProductionMode() ? "false" : "true" ) );
    }

    @GwtIncompatible
    @Override
    boolean areDebugToStringMethodsEnabled()
    {
      return "true".equals( System.getProperty( "galdr.debug_to_string", isProductionMode() ? "false" : "true" ) );
    }

    @GwtIncompatible
    @Override
    boolean areErrorHandlersEnabled()
    {
      return "true".equals( System.getProperty( "galdr.enable_error_handlers", "true" ) );
    }

    @GwtIncompatible
    @Override
    boolean enforceUnmodifiableCollections()
    {
      return "true".equals( System.getProperty( "galdr.enforce_unmodifiable_collections",
                                                isProductionMode() ? "false" : "true" ) );
    }

    @GwtIncompatible
    @Override
    boolean checkInvariants()
    {
      return "true".equals( System.getProperty( "galdr.check_invariants", isProductionMode() ? "false" : "true" ) );
    }

    @GwtIncompatible
    @Override
    boolean checkApiInvariants()
    {
      return "true".equals( System.getProperty( "galdr.check_api_invariants", isProductionMode() ? "false" : "true" ) );
    }

    @GwtIncompatible
    @Override
    @Nonnull
    String loggerType()
    {
      return System.getProperty( "galdr.logger", isProductionMode() ? "basic" : "proxy" );
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

    boolean areSpiesEnabled()
    {
      return "true" == System.getProperty( "galdr.enable_spies" );
    }

    boolean areDebugToStringMethodsEnabled()
    {
      return "true" == System.getProperty( "galdr.debug_to_string" );
    }

    boolean areErrorHandlersEnabled()
    {
      return "true" == System.getProperty( "galdr.enable_error_handlers" );
    }

    boolean enforceUnmodifiableCollections()
    {
      return "true" == System.getProperty( "galdr.enforce_unmodifiable_collections" );
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
       * Valid values are: "none", "console" and "proxy" (for testing)
       */
      return System.getProperty( "galdr.logger" );
    }
  }
}
