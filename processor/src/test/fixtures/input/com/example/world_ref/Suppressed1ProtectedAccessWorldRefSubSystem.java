package com.example.world_ref;

import galdr.World;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;
import galdr.annotations.WorldRef;

@GaldrSubSystem
public abstract class Suppressed1ProtectedAccessWorldRefSubSystem
{
  // This uses the SOURCE retention suppression
  @SuppressWarnings( "Galdr:ProtectedRefMethod" )
  @WorldRef
  protected abstract World world();

  @Processor
  final void runFrame()
  {
  }
}
