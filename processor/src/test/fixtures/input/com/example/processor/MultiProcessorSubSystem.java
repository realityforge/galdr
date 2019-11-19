package com.example.processor;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class MultiProcessorSubSystem
{
  @Processor
  final void runFrame1()
  {
  }

  @Processor
  final void runFrame2()
  {
  }
}
