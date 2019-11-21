package com.example;

import galdr.Stage;
import galdr.annotations.GaldrApplication;
import galdr.annotations.GaldrStage;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;

public class NonStaticInnerClassApplication
{
  @GaldrSubSystem
  static abstract class MySubSystem
  {
    @Processor
    final void runFrame()
    {
    }
  }

  @GaldrApplication
  public abstract class MyApplication
  {
    @GaldrStage( MySubSystem.class )
    abstract Stage sim();
  }

}
