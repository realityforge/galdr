package com.example.processor;

import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
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
