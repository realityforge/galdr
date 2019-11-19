package com.example.on_activate;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.OnActivate;
import galdr.annotations.Processor;

@GaldrSubSystem
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
