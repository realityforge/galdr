package galdr.annotations.test;

import galdr.ProcessorStage;
import galdr.annotations.GaldrApplication;
import galdr.annotations.Stage;
import javax.annotation.Nonnull;

@GaldrApplication
abstract class MyApplication
{
  @Nonnull
  public static MyApplication create()
  {
    return new Galdr_MyApplication();
  }

  @Stage( { BootstrapProcessor.class, HealthProcessor.class } )
  @Nonnull
  abstract ProcessorStage sim();
}
