package com.example.world_ref;

import galdr.World;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;
import galdr.annotations.WorldRef;

@GaldrSubSystem
public abstract class ConcreteWorldRefSubSystem
{
  @WorldRef
  World world()
  {
    return null;
  }

  @Processor
  final void runFrame()
  {
  }
}
