package com.example.component_manager_ref;

import galdr.ComponentManager;
import galdr.annotations.ComponentManagerRef;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class BasicComponentManagerRefSubSystem
{
  public static class MyComponent
  {
  }

  @ComponentManagerRef
  abstract ComponentManager<MyComponent> cm();

  @Processor
  final void runFrame()
  {
  }
}
