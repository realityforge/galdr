package com.example.world_ref;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;
import galdr.annotations.WorldRef;

@GaldrSubSystem
public abstract class BadType4WorldRefSubSystem<T>
{
  @WorldRef
  abstract T world();

  @Processor
  final void runFrame()
  {
  }
}
