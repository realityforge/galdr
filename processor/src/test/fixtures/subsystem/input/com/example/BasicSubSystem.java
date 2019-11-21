package com.example;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class BasicSubSystem
{
  @Processor
  final void runFrame()
  {
  }
}
