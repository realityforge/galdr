package com.example.component;

import galdr.Stage;
import galdr.annotations.Component;
import galdr.annotations.GaldrApplication;
import galdr.annotations.GaldrStage;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;
import javax.annotation.Nonnull;

@GaldrApplication( components = { MultipleComponentApplication.MyComponent1.class,
                                  MultipleComponentApplication.MyComponent2.class,
                                  MultipleComponentApplication.MyComponent3.class } )
abstract class MultipleComponentApplication
{
  @Component
  static class MyComponent1
  {
  }

  @Component
  static class MyComponent2
  {
  }

  @Component
  static class MyComponent3
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
