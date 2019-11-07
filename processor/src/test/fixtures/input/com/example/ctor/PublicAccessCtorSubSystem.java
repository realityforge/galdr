package com.example.ctor;

import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class PublicAccessCtorSubSystem
{
  public PublicAccessCtorSubSystem()
  {
  }

  @Processor
  final void runFrame()
  {
  }
}
