package com.example.component_manager_ref;

import galdr.annotations.ComponentManagerRef;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class VoidComponentManagerRefSubSystem
{
  @ComponentManagerRef
  abstract void cm();

  @Processor
  final void runFrame()
  {
  }
}
