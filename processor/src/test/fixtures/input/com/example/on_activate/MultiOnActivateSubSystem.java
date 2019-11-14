package com.example.on_activate;

import galdr.annotations.OnActivate;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
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
