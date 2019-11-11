package com.example.world_ref;

import galdr.World;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;
import galdr.annotations.WorldRef;

@SubSystem
public abstract class MultiWorldRefSubSystem
{
  @WorldRef
  abstract World world1();

  @WorldRef
  abstract World world2();

  @Processor
  final void runFrame()
  {
  }
}
