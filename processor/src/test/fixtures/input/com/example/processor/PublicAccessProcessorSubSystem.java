package com.example.processor;

import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class PublicAccessProcessorSubSystem
{
  @Processor
  public void runFrame()
  {
  }
}
