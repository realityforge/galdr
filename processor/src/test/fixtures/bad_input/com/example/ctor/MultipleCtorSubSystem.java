package com.example.ctor;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class MultipleCtorSubSystem
{
  MultipleCtorSubSystem()
  {
  }

  MultipleCtorSubSystem( int i )
  {
  }

  @Processor
  final void runFrame()
  {
  }
}
