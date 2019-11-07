package com.example.name_ref;

import galdr.annotations.NameRef;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
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
