package com.example.on_activate;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.OnActivate;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class AbstractOnActivateSubSystem
{
  @OnActivate
  abstract void onActivate();

  @Processor
  final void runFrame()
  {
  }
}
