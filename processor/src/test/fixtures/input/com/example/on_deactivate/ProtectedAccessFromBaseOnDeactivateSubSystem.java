package com.example.on_deactivate;

import com.example.on_deactivate.other.BaseProtectedAccessOnDeactivateSubSystem;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class ProtectedAccessFromBaseOnDeactivateSubSystem
  extends BaseProtectedAccessOnDeactivateSubSystem
{
  @Processor
  final void runFrame()
  {
  }
}
