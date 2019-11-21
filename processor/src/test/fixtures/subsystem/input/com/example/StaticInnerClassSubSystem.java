package com.example;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;

public class StaticInnerClassSubSystem
{
  @GaldrSubSystem
  static abstract class Foo
  {
    @Processor
    final void runFrame()
    {
    }
  }
}
