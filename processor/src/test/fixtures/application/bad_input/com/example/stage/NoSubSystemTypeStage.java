package com.example.stage;

import galdr.Stage;
import galdr.annotations.GaldrApplication;
import galdr.annotations.GaldrStage;
import javax.annotation.Nonnull;

@GaldrApplication
abstract class NoSubSystemTypeStage
{
  @GaldrStage( {} )
  @Nonnull
  abstract Stage sim();
}
