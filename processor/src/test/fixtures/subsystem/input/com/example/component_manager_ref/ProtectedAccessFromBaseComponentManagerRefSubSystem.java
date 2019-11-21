package com.example.component_manager_ref;

import com.example.component_manager_ref.other.BaseProtectedAccessComponentManagerRefSubSystem;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;

@GaldrSubSystem
abstract class ProtectedAccessFromBaseComponentManagerRefSubSystem
  extends BaseProtectedAccessComponentManagerRefSubSystem
{
  @Processor
  final void runFrame()
  {
  }
}
