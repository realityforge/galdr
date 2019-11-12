package com.example.world_ref;

import galdr.World;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;
import galdr.annotations.WorldRef;
import java.io.IOException;

@SubSystem
public abstract class ThrowsExceptionWorldRefSubSystem
{
  @WorldRef
  abstract World world()
    throws IOException;

  @Processor
  final void runFrame()
  {
  }
}
