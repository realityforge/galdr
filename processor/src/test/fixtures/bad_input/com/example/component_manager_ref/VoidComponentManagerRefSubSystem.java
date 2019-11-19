package com.example.component_manager_ref;

import galdr.annotations.ComponentManagerRef;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class VoidComponentManagerRefSubSystem
{
  @ComponentManagerRef
  abstract void cm();

  @Processor
  final void runFrame()
  {
  }
}
