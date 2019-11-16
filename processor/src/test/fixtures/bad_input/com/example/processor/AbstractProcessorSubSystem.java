package com.example.processor;

import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class AbstractProcessorSubSystem
{
  @Processor
  abstract void process();

  @Processor
  final void runFrame()
  {
  }
}
