package com.example.ctor;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;

@GaldrSubSystem
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
