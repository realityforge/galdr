package com.example;

import galdr.Stage;
import galdr.annotations.Component;
import galdr.annotations.GaldrApplication;
import galdr.annotations.GaldrStage;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;

@GaldrApplication( components = InterfaceApplication.MyComponent.class )
public interface InterfaceApplication
{
  @Component
  class MyComponent
  {
  }

  @GaldrSubSystem
  abstract class MySubSystem
  {
    @Processor
    final void runFrame()
    {
    }
  }

  @GaldrStage( MySubSystem.class )
  Stage sim();
}
