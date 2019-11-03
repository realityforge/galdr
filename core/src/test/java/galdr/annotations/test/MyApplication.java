package galdr.annotations.test;

import galdr.ProcessorStage;
import galdr.annotations.GaldrApplication;
import galdr.annotations.StageRef;
import javax.annotation.Nonnull;

@GaldrApplication
abstract class MyApplication
{
  @Nonnull
  public static MyApplication create()
  {
    return new Galdr_MyApplication();
  }

  @StageRef
  @Nonnull
  abstract ProcessorStage sim();
}
