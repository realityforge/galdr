package com.example.processor;

import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class ParameterizedBadCountProcessorSubSystem
{
  @Processor
  void process( int i, int j )
  {
  }

  @Processor
  final void runFrame()
  {
  }
}
