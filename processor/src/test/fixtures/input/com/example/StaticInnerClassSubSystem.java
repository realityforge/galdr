package com.example;

import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

public class StaticInnerClassSubSystem
{
  @SubSystem
  static abstract class Foo
  {
    @Processor
    final void runFrame()
    {
    }
  }
}
