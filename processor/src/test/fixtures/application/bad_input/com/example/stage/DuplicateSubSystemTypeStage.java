package com.example.stage;

import galdr.Stage;
import galdr.annotations.Component;
import galdr.annotations.GaldrApplication;
import galdr.annotations.GaldrStage;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;
import javax.annotation.Nonnull;

@GaldrApplication( components = DuplicateSubSystemTypeStage.MyComponent.class )
abstract class DuplicateSubSystemTypeStage
{
  @Component
  static class MyComponent
  {
  }

  @GaldrSubSystem
  static abstract class MySubSystem1
  {
    @Processor
    final void runFrame()
    {
    }
  }

  @GaldrSubSystem
  static abstract class MySubSystem2
  {
    @Processor
    final void runFrame()
    {
    }
  }

  @GaldrStage( { MySubSystem1.class, MySubSystem2.class, MySubSystem1.class } )
  @Nonnull
  abstract Stage sim();
}
