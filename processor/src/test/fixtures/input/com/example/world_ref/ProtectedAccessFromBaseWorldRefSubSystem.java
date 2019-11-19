package com.example.world_ref;

import com.example.world_ref.other.BaseProtectedAccessWorldRefSubSystem;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class ProtectedAccessFromBaseWorldRefSubSystem
  extends BaseProtectedAccessWorldRefSubSystem
{
  @Processor
  final void runFrame()
  {
  }
}
