package com.example.processor;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;

@GaldrSubSystem
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
