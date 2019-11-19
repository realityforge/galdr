package com.example.on_activate;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.OnActivate;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class BasicOnActivateSubSystem
{
  @OnActivate
  void onActivate()
  {
  }

  @Processor
  final void runFrame()
  {
  }
}
