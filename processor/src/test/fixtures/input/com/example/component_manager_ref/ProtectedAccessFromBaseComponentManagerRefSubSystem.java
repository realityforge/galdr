package com.example.component_manager_ref;

import com.example.component_manager_ref.other.BaseProtectedAccessComponentManagerRefSubSystem;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
abstract class ProtectedAccessFromBaseComponentManagerRefSubSystem
  extends BaseProtectedAccessComponentManagerRefSubSystem
{
  @Processor
  final void runFrame()
  {
  }
}
