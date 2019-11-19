package galdr.annotations.test;

import galdr.Stage;
import galdr.annotations.GaldrApplication;
import galdr.annotations.GaldrStage;
import javax.annotation.Nonnull;

@GaldrApplication
abstract class MyApplication
{
  @Nonnull
  public static MyApplication create()
  {
    return new Galdr_MyApplication();
  }

  @GaldrStage( { BootstrapProcessor.class, HealthProcessor.class } )
  @Nonnull
  abstract Stage sim();
}
