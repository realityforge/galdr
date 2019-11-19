package com.example.processor;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;

@GaldrSubSystem
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
