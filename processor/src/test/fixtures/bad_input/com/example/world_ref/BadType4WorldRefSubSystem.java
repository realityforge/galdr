package com.example.world_ref;

import galdr.annotations.Processor;
import galdr.annotations.SubSystem;
import galdr.annotations.WorldRef;

@SubSystem
public abstract class BadType4WorldRefSubSystem<T>
{
  @WorldRef
  abstract T world();

  @Processor
  final void runFrame()
  {
  }
}
