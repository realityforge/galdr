package com.example.name_ref;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.NameRef;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class MultiNameRefSubSystem
{
  @NameRef
  abstract String name1();

  @NameRef
  abstract String name2();

  @Processor
  final void runFrame()
  {
  }
}
