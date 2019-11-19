package com.example.name_ref;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.NameRef;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class ProtectedAccessNameRefSubSystem
{
  @NameRef
  protected abstract String name();

  @Processor
  final void runFrame()
  {
  }
}
