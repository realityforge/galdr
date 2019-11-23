package com.example.component;

import galdr.Stage;
import galdr.annotations.Component;
import galdr.annotations.GaldrApplication;
import galdr.annotations.GaldrStage;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;
import galdr.annotations.Storage;
import javax.annotation.Nonnull;

@GaldrApplication( components = ExplicitAutodetectComponentTypeApplication.MyComponent.class )
abstract class ExplicitAutodetectComponentTypeApplication
{
  @Component( storage = Storage.AUTODETECT )
  static class MyComponent
  {
    int val;
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
