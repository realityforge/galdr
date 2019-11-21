package com.example.processor;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class PublicAccessViaInterfaceProcessorSubSystem
  implements ProcessorInterface
{
  @Override
  @Processor
  public void runFrame()
  {
  }
}
