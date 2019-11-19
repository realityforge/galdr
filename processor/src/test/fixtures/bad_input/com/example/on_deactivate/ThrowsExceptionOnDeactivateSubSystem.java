package com.example.on_deactivate;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.OnDeactivate;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class ThrowsExceptionOnDeactivateSubSystem
{
  @OnDeactivate
  void onDeactivate()
    throws IndexOutOfBoundsException
  {
  }

  @Processor
  final void runFrame()
  {
  }
}
