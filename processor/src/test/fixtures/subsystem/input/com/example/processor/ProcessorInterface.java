package com.example.processor;

import galdr.annotations.Processor;

public interface ProcessorInterface
{
  @Processor
  default void runFrame()
  {
  }
}
