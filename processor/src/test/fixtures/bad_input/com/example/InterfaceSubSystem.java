package com.example;

import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public interface InterfaceSubSystem
{
  @Processor
  default void runFrame()
  {
  }
}
