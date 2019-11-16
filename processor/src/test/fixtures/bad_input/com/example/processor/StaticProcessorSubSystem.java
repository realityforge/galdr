package com.example.processor;

import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class StaticProcessorSubSystem
{
  @Processor
  static void process()
  {
  }

  @Processor
  final void runFrame()
  {
  }
}
