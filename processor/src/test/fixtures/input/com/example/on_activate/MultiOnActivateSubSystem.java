package com.example.on_activate;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.OnActivate;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class MultiOnActivateSubSystem
{
  @OnActivate
  void onActivate1()
  {
  }

  @OnActivate
  void onActivate2()
  {
  }

  @Processor
  final void runFrame()
  {
  }
}
