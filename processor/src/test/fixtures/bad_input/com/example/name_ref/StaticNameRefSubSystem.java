package com.example.name_ref;

import galdr.annotations.NameRef;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class StaticNameRefSubSystem
{
  @NameRef
  static String name()
  {
    return "";
  }

  @Processor
  final void runFrame()
  {
  }
}
