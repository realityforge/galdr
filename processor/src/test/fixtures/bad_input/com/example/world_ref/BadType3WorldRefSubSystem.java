package com.example.world_ref;

import galdr.annotations.Processor;
import galdr.annotations.SubSystem;
import galdr.annotations.WorldRef;

@SubSystem
public abstract class BadType3WorldRefSubSystem
{
  @WorldRef
  abstract <T> T world();

  @Processor
  final void runFrame()
  {
  }
}
