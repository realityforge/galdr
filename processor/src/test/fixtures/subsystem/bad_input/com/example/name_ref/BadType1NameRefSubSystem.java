package com.example.name_ref;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.NameRef;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class BadType1NameRefSubSystem
{
  @NameRef
  abstract int name();

  @Processor
  final void runFrame()
  {
  }
}
