package com.example.on_deactivate;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.OnDeactivate;
import galdr.annotations.Processor;

@GaldrSubSystem
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
