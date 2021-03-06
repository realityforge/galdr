package com.example.component_manager_ref;

import com.example.component_manager_ref.other.BaseUnreachableComponentManagerRefSubSystem;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class UnreachableComponentManagerRefSubSystem
  extends BaseUnreachableComponentManagerRefSubSystem
{
  @Processor
  final void runFrame()
  {
  }
}
