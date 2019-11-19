package com.example.on_deactivate;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.OnDeactivate;
import galdr.annotations.Processor;

@GaldrSubSystem
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
