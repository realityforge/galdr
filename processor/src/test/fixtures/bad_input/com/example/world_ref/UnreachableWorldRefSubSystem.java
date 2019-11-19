package com.example.world_ref;

import com.example.world_ref.other.BaseUnreachableWorldRefSubSystem;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class UnreachableWorldRefSubSystem
  extends BaseUnreachableWorldRefSubSystem
{
  @Processor
  final void runFrame()
  {
  }
}
