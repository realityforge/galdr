package com.example.world_ref;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;
import galdr.annotations.WorldRef;

@GaldrSubSystem
public abstract class BadType2WorldRefSubSystem
{
  @WorldRef
  abstract String world();

  @Processor
  final void runFrame()
  {
  }
}
