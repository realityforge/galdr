package com.example.on_activate;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.OnActivate;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class ProtectedAccessOnActivateSubSystem
{
  @OnActivate
  protected void onActivate()
  {
  }

  @Processor
  final void runFrame()
  {
  }
}
