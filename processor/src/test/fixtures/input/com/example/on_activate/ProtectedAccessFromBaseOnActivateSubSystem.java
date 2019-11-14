package com.example.on_activate;

import com.example.on_activate.other.BaseProtectedAccessOnActivateSubSystem;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class ProtectedAccessFromBaseOnActivateSubSystem
  extends BaseProtectedAccessOnActivateSubSystem
{
  @Processor
  final void runFrame()
  {
  }
}
