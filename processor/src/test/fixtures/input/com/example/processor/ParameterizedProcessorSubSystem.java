package com.example.processor;

import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class ParameterizedProcessorSubSystem
{
  @Processor
  final void runFrame( int delta )
  {
  }
}
