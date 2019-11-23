package com.example.stage;

import galdr.Stage;
import galdr.annotations.Component;
import galdr.annotations.GaldrApplication;
import galdr.annotations.GaldrStage;
import galdr.annotations.Processor;
import javax.annotation.Nonnull;

@GaldrApplication( components = BadSubSystemType1Stage.MyComponent.class )
abstract class BadSubSystemType1Stage
{
  @Component
  static class MyComponent
  {
  }

  static abstract class MySubSystem
  {
    @Processor
    final void runFrame()
    {
    }
  }

  @GaldrStage( MySubSystem.class )
  @Nonnull
  abstract Stage sim();
}
