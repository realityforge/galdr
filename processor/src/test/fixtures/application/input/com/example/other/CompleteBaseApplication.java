package com.example.other;

import galdr.Stage;
import galdr.annotations.Component;
import galdr.annotations.GaldrStage;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;
import javax.annotation.Nonnull;

public abstract class CompleteBaseApplication
{
  @Component
  public static class MyComponent
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
  protected abstract Stage sim();

  @GaldrStage( MySubSystem3.class )
  @Nonnull
  protected abstract Stage render();
}
