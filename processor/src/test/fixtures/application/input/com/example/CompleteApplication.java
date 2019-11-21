package com.example;

import galdr.Stage;
import galdr.annotations.GaldrApplication;
import galdr.annotations.GaldrStage;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;
import javax.annotation.Nonnull;

@GaldrApplication
public abstract class CompleteApplication
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

  @GaldrSubSystem
  static abstract class MySubSystem3
  {
    @Processor
    final void runFrame()
    {
    }
  }

  @GaldrStage( { MySubSystem1.class, MySubSystem2.class } )
  @Nonnull
  abstract Stage sim();

  @GaldrStage( MySubSystem3.class )
  @Nonnull
  abstract Stage render();

  //TODO: Add Components to example
}
