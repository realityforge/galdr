package com.example.on_deactivate;

import galdr.annotations.OnDeactivate;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class ParameterizedOnDeactivateSubSystem
{
  @OnDeactivate
  void onDeactivate( int i )
  {
  }

  @Processor
  final void runFrame()
  {
  }
}
