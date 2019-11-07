package com.example;

import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class BasicSubSystem
{
  @Processor
  final void runFrame()
  {
  }
}
