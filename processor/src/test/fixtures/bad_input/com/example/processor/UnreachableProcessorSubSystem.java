package com.example.processor;

import com.example.processor.other.BaseUnreachableProcessorSubSystem;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class UnreachableProcessorSubSystem
  extends BaseUnreachableProcessorSubSystem
{
  @Processor
  final void runFrame()
  {
  }
}
