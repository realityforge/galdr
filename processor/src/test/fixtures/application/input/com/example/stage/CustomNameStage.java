package com.example.stage;

import galdr.Stage;
import galdr.annotations.Component;
import galdr.annotations.GaldrApplication;
import galdr.annotations.GaldrStage;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;
import javax.annotation.Nonnull;

@GaldrApplication( components = CustomNameStage.MyComponent.class )
abstract class CustomNameStage
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

  @GaldrStage( value = MySubSystem.class, name = "monkey" )
  @Nonnull
  abstract Stage sim();
}
