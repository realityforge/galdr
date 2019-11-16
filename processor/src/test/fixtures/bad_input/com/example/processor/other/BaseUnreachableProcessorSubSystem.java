package com.example.processor.other;

import galdr.annotations.Processor;
import java.io.IOException;

public abstract class BaseUnreachableProcessorSubSystem
{
  @Processor
  void process()
    throws IOException
  {
  }
}
