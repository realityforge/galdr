package com.example.world_ref;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;
import galdr.annotations.WorldRef;

@GaldrSubSystem
public abstract class VoidWorldRefSubSystem
{
  @WorldRef
  abstract void world();

  @Processor
  final void runFrame()
  {
  }
}
