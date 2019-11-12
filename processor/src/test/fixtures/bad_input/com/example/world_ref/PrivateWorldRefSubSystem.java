package com.example.world_ref;

import galdr.World;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;
import galdr.annotations.WorldRef;

@SubSystem
public abstract class PrivateWorldRefSubSystem
{
  @WorldRef
  private World world()
  {
    return null;
  }

  @Processor
  final void runFrame()
  {
  }
}
