package com.example.world_ref;

import galdr.annotations.Processor;
import galdr.annotations.SubSystem;
import galdr.annotations.WorldRef;

@SubSystem
public abstract class VoidWorldRefSubSystem
{
  @WorldRef
  abstract void world();

  @Processor
  final void runFrame()
  {
  }
}
