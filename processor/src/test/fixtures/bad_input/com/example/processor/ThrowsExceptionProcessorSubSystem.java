package com.example.processor;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;
import java.io.IOException;

@GaldrSubSystem
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
