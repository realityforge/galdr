package galdr.annotations.test;

import galdr.ComponentAPI;
import galdr.ProcessorStage;
import galdr.World;
import galdr.annotations.ComponentRef;
import galdr.annotations.GaldrApplication;
import galdr.annotations.StageRef;
import galdr.annotations.WorldRef;
import javax.annotation.Nonnull;

@GaldrApplication
abstract class MyApplication
{
  @Nonnull
  public static MyApplication create()
  {
    return new Galdr_MyApplication();
  }

  @WorldRef
  @Nonnull
  abstract World world();

  @StageRef
  @Nonnull
  abstract ProcessorStage sim();

  @ComponentRef
  @Nonnull
  abstract ComponentAPI<Health> health();
}
