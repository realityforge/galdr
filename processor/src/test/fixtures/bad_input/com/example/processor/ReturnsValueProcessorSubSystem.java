package com.example.processor;

import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class ReturnsValueProcessorSubSystem
{
  @Processor
  String process()
  {
    return null;
  }

  @Processor
  final void runFrame()
  {
  }
}
