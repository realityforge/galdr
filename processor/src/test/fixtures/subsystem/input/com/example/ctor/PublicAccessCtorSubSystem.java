package com.example.ctor;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;

@GaldrSubSystem
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
