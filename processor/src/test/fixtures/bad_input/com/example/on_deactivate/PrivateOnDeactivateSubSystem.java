package com.example.on_deactivate;

import galdr.annotations.OnDeactivate;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class PrivateOnDeactivateSubSystem
{
  @OnDeactivate
  private void onDeactivate()
  {
  }

  @Processor
  final void runFrame()
  {
  }
}
