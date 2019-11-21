package com.example.other;

import galdr.Stage;
import galdr.annotations.GaldrApplication;
import galdr.annotations.GaldrStage;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;
import javax.annotation.Nonnull;

public interface CompleteInterfaceApplication
{
  @GaldrSubSystem
  abstract class MySubSystem1
  {
    @Processor
    final void runFrame()
    {
    }
  }

  @GaldrSubSystem
  abstract class MySubSystem2
  {
    @Processor
    final void runFrame()
    {
    }
  }

  @GaldrSubSystem
  abstract class MySubSystem3
  {
    @Processor
    final void runFrame()
    {
    }
  }

  @GaldrStage( { MySubSystem1.class, MySubSystem2.class } )
  @Nonnull
  Stage sim();

  @GaldrStage( MySubSystem3.class )
  @Nonnull
  Stage render();

  //TODO: Add Components to example
}
