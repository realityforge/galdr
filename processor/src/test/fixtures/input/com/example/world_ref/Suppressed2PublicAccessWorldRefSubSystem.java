package com.example.world_ref;

import galdr.World;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;
import galdr.annotations.SuppressGaldrWarnings;
import galdr.annotations.WorldRef;

@SubSystem
public abstract class Suppressed2PublicAccessWorldRefSubSystem
{
  // This uses the CLASS retention suppression
  @SuppressGaldrWarnings( "Galdr:PublicRefMethod" )
  @WorldRef
  public abstract World world();

  @Processor
  final void runFrame()
  {
  }
}
