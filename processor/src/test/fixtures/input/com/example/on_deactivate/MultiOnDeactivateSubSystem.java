package com.example.on_deactivate;

import galdr.annotations.OnDeactivate;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class MultiOnDeactivateSubSystem
{
  @OnDeactivate
  void onDeactivate1()
  {
  }

  @OnDeactivate
  void onDeactivate2()
  {
  }

  @Processor
  final void runFrame()
  {
  }
}
