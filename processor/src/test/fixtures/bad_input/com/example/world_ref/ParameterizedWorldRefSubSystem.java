package com.example.world_ref;

import galdr.World;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;
import galdr.annotations.WorldRef;

@SubSystem
public abstract class ParameterizedWorldRefSubSystem
{
  @WorldRef
  abstract World world( int i );

  @Processor
  final void runFrame()
  {
  }
}
