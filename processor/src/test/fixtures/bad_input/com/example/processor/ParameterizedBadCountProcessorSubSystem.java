package com.example.processor;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;

@GaldrSubSystem
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
