package com.example.on_deactivate;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.OnDeactivate;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class PublicAccessOnDeactivateSubSystem
{
  @OnDeactivate
  public void onDeactivate()
  {
  }

  @Processor
  final void runFrame()
  {
  }
}
