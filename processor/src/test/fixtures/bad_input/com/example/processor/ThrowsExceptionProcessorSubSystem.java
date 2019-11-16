package com.example.processor;

import galdr.annotations.Processor;
import galdr.annotations.SubSystem;
import java.io.IOException;

@SubSystem
public abstract class ThrowsExceptionProcessorSubSystem
{
  @Processor
  void process()
    throws IOException
  {
  }

  @Processor
  final void runFrame()
  {
  }
}
