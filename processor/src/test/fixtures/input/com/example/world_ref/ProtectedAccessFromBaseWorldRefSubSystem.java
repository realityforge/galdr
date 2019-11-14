package com.example.world_ref;

import com.example.world_ref.other.BaseProtectedAccessWorldRefSubSystem;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class ProtectedAccessFromBaseWorldRefSubSystem
  extends BaseProtectedAccessWorldRefSubSystem
{
  @Processor
  final void runFrame()
  {
  }
}
