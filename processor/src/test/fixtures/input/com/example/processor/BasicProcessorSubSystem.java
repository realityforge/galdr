package com.example.processor;

import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class BasicProcessorSubSystem
{
  @Processor
  final void runFrame()
  {
  }
}
