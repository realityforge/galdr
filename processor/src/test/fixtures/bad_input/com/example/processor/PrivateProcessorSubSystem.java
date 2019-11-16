package com.example.processor;

import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
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
