package com.example.on_activate;

import galdr.annotations.OnActivate;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class PrivateOnActivateSubSystem
{
  @OnActivate
  private void onActivate()
  {
  }

  @Processor
  final void runFrame()
  {
  }
}
