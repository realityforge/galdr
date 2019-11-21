package com.example.name_ref;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.NameRef;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class BadType4NameRefSubSystem<T>
{
  @NameRef
  abstract T name();

  @Processor
  final void runFrame()
  {
  }
}
