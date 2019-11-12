package com.example.component_manager_ref;

import galdr.ComponentManager;
import galdr.annotations.ComponentManagerRef;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class StaticComponentManagerRefSubSystem
{
  @ComponentManagerRef
  static ComponentManager<String> cm()
  {
    return null;
  }

  @Processor
  final void runFrame()
  {
  }
}
