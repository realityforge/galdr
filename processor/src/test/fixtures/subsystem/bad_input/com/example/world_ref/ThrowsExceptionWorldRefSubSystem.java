package com.example.world_ref;

import galdr.World;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;
import galdr.annotations.WorldRef;
import java.io.IOException;

@GaldrSubSystem
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
