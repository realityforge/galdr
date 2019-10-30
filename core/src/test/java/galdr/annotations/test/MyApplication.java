package galdr.annotations.test;

import galdr.ComponentManager;
import galdr.ProcessorStage;
import galdr.World;
import galdr.annotations.ComponentManagerRef;
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

  @ComponentManagerRef
  @Nonnull
  abstract ComponentManager<Health> health();
}
