package com.example.world_ref;

import galdr.World;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;
import galdr.annotations.WorldRef;

@SubSystem
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
