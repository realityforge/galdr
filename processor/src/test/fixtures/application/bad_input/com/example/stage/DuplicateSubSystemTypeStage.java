package com.example.stage;

import galdr.Stage;
import galdr.annotations.GaldrApplication;
import galdr.annotations.GaldrStage;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;
import javax.annotation.Nonnull;

@GaldrApplication
abstract class DuplicateSubSystemTypeStage
{
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
