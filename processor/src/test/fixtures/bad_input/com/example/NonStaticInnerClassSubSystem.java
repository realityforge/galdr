package com.example;

import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

public class NonStaticInnerClassSubSystem
{
  @SubSystem
  abstract class Foo
  {
    @Processor
    final void runFrame()
    {
    }
  }
}
