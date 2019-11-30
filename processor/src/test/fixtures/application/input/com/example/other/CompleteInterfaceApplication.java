package com.example.other;

import galdr.Stage;
import galdr.annotations.Component;
import galdr.annotations.GaldrStage;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;
import javax.annotation.Nonnull;

public interface CompleteInterfaceApplication
{
  @Component
  class MyComponent
  {
  }

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
}
