package com.example.processor;

import com.example.processor.other.BaseUnreachableProcessorSubSystem;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class UnreachableProcessorSubSystem
  extends BaseUnreachableProcessorSubSystem
{
  @Processor
  final void runFrame()
  {
  }
}
