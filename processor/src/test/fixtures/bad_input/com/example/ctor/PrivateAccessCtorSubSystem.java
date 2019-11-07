package com.example.ctor;

import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class PrivateAccessCtorSubSystem
{
  private PrivateAccessCtorSubSystem()
  {
  }

  @Processor
  final void runFrame()
  {
  }
}
