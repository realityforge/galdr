package com.example.name_ref;

import galdr.annotations.NameRef;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class VoidNameRefSubSystem
{
  @NameRef
  abstract void name();

  @Processor
  final void runFrame()
  {
  }
}
