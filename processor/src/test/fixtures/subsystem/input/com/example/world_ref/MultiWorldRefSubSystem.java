package com.example.world_ref;

import galdr.World;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;
import galdr.annotations.WorldRef;

@GaldrSubSystem
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
