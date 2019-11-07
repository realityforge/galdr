package com.example.ctor;

import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
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
