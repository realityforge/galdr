package galdr;

import java.lang.reflect.Field;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Utility class for interacting with Galdr config settings in tests.
 */
@SuppressWarnings( "WeakerAccess" )
@GwtIncompatible
public final class GaldrTestUtil
{
  private GaldrTestUtil()
  {
  }

  /**
   * Interface to intercept log messages emitted by the runtime.
   */
  public interface Logger
  {
    void log( @Nonnull String message, @Nullable Throwable throwable );
  }

  /**
   * Reset the state of Galdr config to either production or development state.
   *
   * @param productionMode true to set it to production mode configuration, false to set it to development mode config.
   */
  public static void resetConfig( final boolean productionMode )
  {
    if ( GaldrConfig.isProductionMode() )
    {
      /*
       * This should never happen.
       */
      throw new IllegalStateException( "Unable to reset config as Galdr is in production mode" );
    }

    if ( productionMode )
    {
      disableNames();
      disableSpies();
      disableDebugToString();
      noEnforceUnmodifiableCollections();
      noCheckInvariants();
      noCheckApiInvariants();
    }
    else
    {
      enableNames();
      enableSpies();
      enableDebugToString();
      enforceUnmodifiableCollections();
      checkInvariants();
      checkApiInvariants();
    }
    enableErrorHandlers();
    setLogger( null );
    resetState();
  }

  /**
   * Clear out any lingering state.
   */
  private static void resetState()
  {
    if ( WorldHolder.isActive() )
    {
      WorldHolder.deactivateWorld( WorldHolder.world() );
    }
    World.resetId();
  }

  /**
   * Specify logger to use to capture logging in tests
   *
   * @param logger the logger.
   */
  public static void setLogger( @Nullable final Logger logger )
  {
    if ( GaldrConfig.isProductionMode() )
    {
      /*
       * This should never happen.
       */
      throw new IllegalStateException( "Unable to call GaldrTestUtil.setLogger() as Galdr is in production mode" );
    }

    final GaldrLogger.ProxyLogger proxyLogger = (GaldrLogger.ProxyLogger) GaldrLogger.getLogger();
    proxyLogger.setLogger( null == logger ? null : logger::log );
  }

  /**
   * Set `galdr.enable_names` setting to true.
   */
  public static void enableNames()
  {
    setEnableNames( true );
  }

  /**
   * Set `galdr.enable_names` setting to false.
   */
  public static void disableNames()
  {
    setEnableNames( false );
  }

  /**
   * Configure the `galdr.enable_names` setting.
   *
   * @param value the setting.
   */
  private static void setEnableNames( final boolean value )
  {
    setConstant( "ENABLE_NAMES", value );
  }

  /**
   * Set `galdr.enable_spies` setting to true.
   */
  public static void enableSpies()
  {
    setEnableSpies( true );
  }

  /**
   * Set `galdr.enable_spies` setting to false.
   */
  public static void disableSpies()
  {
    setEnableSpies( false );
  }

  /**
   * Configure the "galdr.enable_spies" setting.
   *
   * @param value the setting.
   */
  private static void setEnableSpies( final boolean value )
  {
    setConstant( "ENABLE_SPIES", value );
  }

  /**
   * Set `galdr.debug_to_string` setting to true.
   */
  public static void enableDebugToString()
  {
    setDebugToString( true );
  }

  /**
   * Set `galdr.debug_to_string` setting to false.
   */
  public static void disableDebugToString()
  {
    setDebugToString( false );
  }

  /**
   * Configure the `galdr.debug_to_string` setting.
   *
   * @param value the setting.
   */
  private static void setDebugToString( final boolean value )
  {
    setConstant( "DEBUG_TO_STRING", value );
  }

  /**
   * Set `galdr.enable_error_handlers` setting to true.
   */
  public static void enableErrorHandlers()
  {
    setEnableErrorHandlers( true );
  }

  /**
   * Set `galdr.enable_error_handlers` setting to false.
   */
  public static void disableErrorHandlers()
  {
    setEnableErrorHandlers( false );
  }

  /**
   * Configure the `galdr.enable_error_handlers` setting.
   *
   * @param value the setting.
   */
  private static void setEnableErrorHandlers( final boolean value )
  {
    setConstant( "ENABLE_ERROR_HANDLERS", value );
  }

  /**
   * Set `galdr.enforce_unmodifiable_collections` setting to true.
   */
  public static void enforceUnmodifiableCollections()
  {
    setEnforceUnmodifiableCollections( true );
  }

  /**
   * Set the `galdr.enforce_unmodifiable_collections` setting to false.
   */
  public static void noEnforceUnmodifiableCollections()
  {
    setEnforceUnmodifiableCollections( false );
  }

  /**
   * Configure the `galdr.enforce_unmodifiable_collections` setting.
   *
   * @param checkInvariants the "check invariants" setting.
   */
  private static void setEnforceUnmodifiableCollections( final boolean checkInvariants )
  {
    setConstant( "ENFORCE_UNMODIFIABLE_COLLECTIONS", checkInvariants );
  }

  /**
   * Set `galdr.check_invariants` setting to true.
   */
  public static void checkInvariants()
  {
    setCheckInvariants( true );
  }

  /**
   * Set the `galdr.check_invariants` setting to false.
   */
  public static void noCheckInvariants()
  {
    setCheckInvariants( false );
  }

  /**
   * Configure the `galdr.check_invariants` setting.
   *
   * @param checkInvariants the "check invariants" setting.
   */
  private static void setCheckInvariants( final boolean checkInvariants )
  {
    setConstant( "CHECK_INVARIANTS", checkInvariants );
  }

  /**
   * Set `galdr.check_api_invariants` setting to true.
   */
  public static void checkApiInvariants()
  {
    setCheckApiInvariants( true );
  }

  /**
   * Set the `galdr.check_api_invariants` setting to false.
   */
  public static void noCheckApiInvariants()
  {
    setCheckApiInvariants( false );
  }

  /**
   * Configure the `galdr.check_api_invariants` setting.
   *
   * @param checkApiInvariants the "check invariants" setting.
   */
  private static void setCheckApiInvariants( final boolean checkApiInvariants )
  {
    setConstant( "CHECK_API_INVARIANTS", checkApiInvariants );
  }

  /**
   * Set the specified field name on GaldrConfig.
   */
  @SuppressWarnings( "NonJREEmulationClassesInClientCode" )
  private static void setConstant( @Nonnull final String fieldName, final boolean value )
  {
    if ( GaldrConfig.isProductionMode() )
    {
      /*
       * This should never happen.
       */
      throw new IllegalStateException( "Unable to change constant " + fieldName + " as Galdr is in production mode" );
    }
    else
    {
      try
      {
        final Field field = GaldrConfig.class.getDeclaredField( fieldName );
        field.setAccessible( true );
        field.set( null, value );
      }
      catch ( final NoSuchFieldException | IllegalAccessException e )
      {
        throw new IllegalStateException( "Unable to change constant " + fieldName, e );
      }
    }
  }
}
