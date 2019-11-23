package com.example;

import galdr.Stage;
import galdr.annotations.Component;
import galdr.annotations.GaldrApplication;
import galdr.annotations.GaldrStage;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;

public class NonStaticInnerClassApplication
{
  @Component
  static class MyComponent
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

  @GaldrApplication( components = MyComponent.class )
  public abstract class MyApplication
  {
    @GaldrStage( MySubSystem.class )
    abstract Stage sim();
  }

}
