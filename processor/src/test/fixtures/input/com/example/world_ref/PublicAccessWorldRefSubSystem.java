package com.example.world_ref;

import galdr.World;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;
import galdr.annotations.WorldRef;

@SubSystem
public abstract class PublicAccessWorldRefSubSystem
{
  @WorldRef
  public abstract World world();

  @Processor
  final void runFrame()
  {
  }
}
