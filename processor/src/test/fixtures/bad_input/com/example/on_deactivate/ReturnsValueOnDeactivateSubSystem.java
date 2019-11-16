package com.example.on_deactivate;

import galdr.annotations.OnDeactivate;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class ReturnsValueOnDeactivateSubSystem
{
  @OnDeactivate
  String onDeactivate()
  {
    return null;
  }

  @Processor
  final void runFrame()
  {
  }
}
