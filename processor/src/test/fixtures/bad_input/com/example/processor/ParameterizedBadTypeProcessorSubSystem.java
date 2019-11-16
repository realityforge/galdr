package com.example.processor;

import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
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
