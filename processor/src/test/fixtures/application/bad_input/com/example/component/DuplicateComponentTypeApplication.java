package com.example.component;

import galdr.Stage;
import galdr.annotations.Component;
import galdr.annotations.GaldrApplication;
import galdr.annotations.GaldrStage;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;
import javax.annotation.Nonnull;

@GaldrApplication( components = { DuplicateComponentTypeApplication.MyComponent1.class,
                                  DuplicateComponentTypeApplication.MyComponent2.class,
                                  DuplicateComponentTypeApplication.MyComponent1.class } )
abstract class DuplicateComponentTypeApplication
{
  @Component
  static class MyComponent1
  {
  }

  @Component
  static class MyComponent2
  {
  }

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
