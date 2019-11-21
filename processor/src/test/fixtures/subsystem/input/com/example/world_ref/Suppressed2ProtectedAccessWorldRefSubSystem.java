package com.example.world_ref;

import galdr.World;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;
import galdr.annotations.SuppressGaldrWarnings;
import galdr.annotations.WorldRef;

@GaldrSubSystem
public abstract class Suppressed2ProtectedAccessWorldRefSubSystem
{
  // This uses the CLASS retention suppression
  @SuppressGaldrWarnings( "Galdr:ProtectedRefMethod" )
  @WorldRef
  protected abstract World world();

  @Processor
  final void runFrame()
  {
  }
}
