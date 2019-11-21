package com.example.name_ref;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.NameRef;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class VoidNameRefSubSystem
{
  @NameRef
  abstract void name();

  @Processor
  final void runFrame()
  {
  }
}
