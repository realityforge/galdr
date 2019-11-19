package com.example.name_ref;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.NameRef;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class PrivateNameRefSubSystem
{
  @NameRef
  private String name()
  {
    return "";
  }

  @Processor
  final void runFrame()
  {
  }
}
