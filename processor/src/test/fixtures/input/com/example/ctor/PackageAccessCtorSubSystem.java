package com.example.ctor;

import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class PackageAccessCtorSubSystem
{
  PackageAccessCtorSubSystem()
  {
  }

  @Processor
  final void runFrame()
  {
  }
}
