package com.example.processor;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class ParameterizedBadTypeProcessorSubSystem
{
  @Processor
  void process( String s )
  {
  }

  @Processor
  final void runFrame()
  {
  }
}
