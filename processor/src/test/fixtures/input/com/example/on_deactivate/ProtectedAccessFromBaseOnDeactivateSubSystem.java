package com.example.on_deactivate;

import com.example.on_deactivate.other.BaseProtectedAccessOnDeactivateSubSystem;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class ProtectedAccessFromBaseOnDeactivateSubSystem
  extends BaseProtectedAccessOnDeactivateSubSystem
{
  @Processor
  final void runFrame()
  {
  }
}
