package com.example.processor;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class ParameterizedProcessorSubSystem
{
  @Processor
  final void runFrame( int delta )
  {
  }
}
