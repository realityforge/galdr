package com.example.processor;

import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class PackageAccessProcessorSubSystem
{
  @Processor
  final void runFrame()
  {
  }
}
