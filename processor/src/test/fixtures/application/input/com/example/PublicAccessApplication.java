package com.example;

import galdr.Stage;
import galdr.annotations.Component;
import galdr.annotations.GaldrApplication;
import galdr.annotations.GaldrStage;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;
import javax.annotation.Nonnull;

@SuppressWarnings( "WeakerAccess" )
@GaldrApplication( components = PublicAccessApplication.MyComponent.class )
public abstract class PublicAccessApplication
{
  @Component
  static class MyComponent
  {
  }

  @GaldrSubSystem
  public static abstract class MySubSystem
  {
    @Processor
    final void runFrame()
    {
    }
  }

  @GaldrStage( MySubSystem.class )
  @Nonnull
  public abstract Stage sim();
}
