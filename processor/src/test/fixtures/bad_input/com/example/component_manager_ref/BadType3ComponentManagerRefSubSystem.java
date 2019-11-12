package com.example.component_manager_ref;

import galdr.annotations.ComponentManagerRef;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class BadType3ComponentManagerRefSubSystem
{
  @ComponentManagerRef
  abstract String cm();

  @Processor
  final void runFrame()
  {
  }
}
