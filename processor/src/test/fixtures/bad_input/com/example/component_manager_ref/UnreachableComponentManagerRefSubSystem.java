package com.example.component_manager_ref;

import com.example.component_manager_ref.other.BaseUnreachableComponentManagerRefSubSystem;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class UnreachableComponentManagerRefSubSystem
  extends BaseUnreachableComponentManagerRefSubSystem
{
  @Processor
  final void runFrame()
  {
  }
}
