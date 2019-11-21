package com.example.on_activate;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.OnActivate;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class ParameterizedOnActivateSubSystem
{
  @OnActivate
  final void onActivate( int i )
  {
  }

  @Processor
  final void runFrame()
  {
  }
}
