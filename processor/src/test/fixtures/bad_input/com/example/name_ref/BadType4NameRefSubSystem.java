package com.example.name_ref;

import galdr.annotations.NameRef;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class BadType4NameRefSubSystem<T>
{
  @NameRef
  abstract T name();

  @Processor
  final void runFrame()
  {
  }
}
