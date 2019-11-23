package com.example.stage;

import galdr.Stage;
import galdr.annotations.Component;
import galdr.annotations.GaldrApplication;
import galdr.annotations.GaldrStage;
import javax.annotation.Nonnull;

@GaldrApplication( components = NoSubSystemTypeStage.MyComponent.class )
abstract class NoSubSystemTypeStage
{
  @Component
  static class MyComponent
  {
  }

  @GaldrStage( {} )
  @Nonnull
  abstract Stage sim();
}
