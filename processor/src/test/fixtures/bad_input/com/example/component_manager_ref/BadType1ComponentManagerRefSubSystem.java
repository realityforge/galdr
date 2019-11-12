package com.example.component_manager_ref;

import galdr.ComponentManager;
import galdr.annotations.ComponentManagerRef;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class BadType1ComponentManagerRefSubSystem
{
  @ComponentManagerRef
  abstract ComponentManager<?> cm();

  @Processor
  final void runFrame()
  {
  }
}
