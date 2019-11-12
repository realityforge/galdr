package com.example.world_ref;

import com.example.world_ref.other.BaseUnreachableWorldRefSubSystem;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class UnreachableWorldRefSubSystem
  extends BaseUnreachableWorldRefSubSystem
{
  @Processor
  final void runFrame()
  {
  }
}
