package com.example.processor;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class AbstractProcessorSubSystem
{
  @Processor
  abstract void process();

  @Processor
  final void runFrame()
  {
  }
}
