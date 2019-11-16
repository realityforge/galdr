package com.example.on_deactivate;

import galdr.annotations.OnDeactivate;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
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
