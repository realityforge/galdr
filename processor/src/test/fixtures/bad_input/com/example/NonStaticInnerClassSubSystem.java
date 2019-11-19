package com.example;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;

public class NonStaticInnerClassSubSystem
{
  @GaldrSubSystem
  abstract class Foo
  {
    @Processor
    final void runFrame()
    {
    }
  }
}
