package com.example.component_manager_ref;

import galdr.ComponentManager;
import galdr.annotations.ComponentManagerRef;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class ParameterizedComponentManagerRefSubSystem
{
  @ComponentManagerRef
  abstract ComponentManager<String> cm( int i );

  @Processor
  final void runFrame()
  {
  }
}
