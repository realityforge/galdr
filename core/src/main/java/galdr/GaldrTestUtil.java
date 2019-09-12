package galdr;

import java.lang.reflect.Field;
import javax.annotation.Nonnull;

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
   * Reset the state of Galdr config to either production or development state.
   *
   * @param productionMode true to set it to production mode configuration, false to set it to development mode config.
   */
  public static void resetConfig( final boolean productionMode )
  {
    if ( GaldrConfig.isProductionMode() )
    {
      /*
       * This should really never happen but if it does add assertion (so code stops in debugger) or
       * failing that throw an exception.
       */
      assert GaldrConfig.isDevelopmentMode();
      throw new IllegalStateException( "Unable to reset config as Galdr is in production mode" );
    }

    if ( productionMode )
    {
      disableDebugToString();
      noCheckInvariants();
      noCheckApiInvariants();
    }
    else
    {
      enableDebugToString();
      checkInvariants();
      checkApiInvariants();
    }
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
       * This should really never happen but if it does add assertion (so code stops in debugger) or
       * failing that throw an exception.
       */
      assert GaldrConfig.isDevelopmentMode();
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