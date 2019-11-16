package com.example.processor;

import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class ProtectedAccessProcessorSubSystem
{
  @Processor
  protected void runFrame()
  {
  }
}
