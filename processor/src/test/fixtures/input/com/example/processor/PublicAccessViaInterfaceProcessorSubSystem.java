package com.example.processor;

import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class PublicAccessViaInterfaceProcessorSubSystem
  implements ProcessorInterface
{
  @Override
  @Processor
  public void runFrame()
  {
  }
}
