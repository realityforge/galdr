package com.example.processor;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class PrivateProcessorSubSystem
{
  @Processor
  private void process()
  {
  }

  @Processor
  final void runFrame()
  {
  }
}
