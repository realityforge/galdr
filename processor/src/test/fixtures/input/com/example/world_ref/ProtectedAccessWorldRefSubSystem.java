package com.example.world_ref;

import galdr.World;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;
import galdr.annotations.WorldRef;

@SubSystem
public abstract class ProtectedAccessWorldRefSubSystem
{
  @WorldRef
  protected abstract World world();

  @Processor
  final void runFrame()
  {
  }
}
