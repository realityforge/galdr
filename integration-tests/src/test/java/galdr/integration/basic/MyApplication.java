package galdr.integration.basic;

import galdr.Stage;
import galdr.annotations.GaldrApplication;
import galdr.annotations.GaldrStage;
import javax.annotation.Nonnull;

@GaldrApplication( components = { Health.class, MyFlag.class } )
abstract class MyApplication
{
  @Nonnull
  static MyApplication create()
  {
    return new Galdr_MyApplication_Example();
  }

  @GaldrStage( { BootstrapProcessor.class, HealthProcessor.class } )
  @Nonnull
  abstract Stage sim();
}
