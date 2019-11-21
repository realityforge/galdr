package com.example;

import galdr.Stage;
import galdr.annotations.GaldrApplication;
import galdr.annotations.GaldrStage;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;
import javax.annotation.Nonnull;

final class StaticInnerClassApplication
{
  @GaldrApplication
  static abstract class BasicApplication
  {
    @GaldrSubSystem
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

}
