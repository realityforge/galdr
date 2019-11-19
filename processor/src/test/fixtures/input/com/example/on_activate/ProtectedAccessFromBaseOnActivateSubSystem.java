package com.example.on_activate;

import com.example.on_activate.other.BaseProtectedAccessOnActivateSubSystem;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class ProtectedAccessFromBaseOnActivateSubSystem
  extends BaseProtectedAccessOnActivateSubSystem
{
  @Processor
  final void runFrame()
  {
  }
}
