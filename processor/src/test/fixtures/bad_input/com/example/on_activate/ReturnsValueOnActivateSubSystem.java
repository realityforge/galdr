package com.example.on_activate;

import galdr.annotations.OnActivate;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class ReturnsValueOnActivateSubSystem
{
  @OnActivate
  Object onActivate()
  {
    return null;
  }

  @Processor
  final void runFrame()
  {
  }
}
