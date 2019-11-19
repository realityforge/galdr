package com.example.processor;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class ProtectedAccessProcessorSubSystem
{
  @Processor
  protected void runFrame()
  {
  }
}
