package com.example.stage;

import galdr.Stage;
import galdr.annotations.GaldrApplication;
import galdr.annotations.GaldrStage;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;
import javax.annotation.Nonnull;

@GaldrApplication
abstract class BadSubSystemType3Stage
{
  @GaldrSubSystem
  static abstract class MySubSystem
  {
    @Processor
    final void runFrame()
    {
    }
  }

  @GaldrStage( { MySubSystem.class, int.class } )
  @Nonnull
  abstract Stage sim();
}
