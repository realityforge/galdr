package com.example.component_manager_ref;

import galdr.ComponentManager;
import galdr.annotations.ComponentManagerRef;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class ConcreteComponentManagerRefSubSystem
{
  @ComponentManagerRef
  ComponentManager<String> cm()
  {
    return null;
  }

  @Processor
  final void runFrame()
  {
  }
}
