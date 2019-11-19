package com.example.ctor;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;

@GaldrSubSystem
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
