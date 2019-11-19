package com.example;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;

@GaldrSubSystem
public interface InterfaceSubSystem
{
  @Processor
  default void runFrame()
  {
  }
}
