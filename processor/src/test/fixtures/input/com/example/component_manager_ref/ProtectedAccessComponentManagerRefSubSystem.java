package com.example.component_manager_ref;

import galdr.ComponentManager;
import galdr.annotations.ComponentManagerRef;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class ProtectedAccessComponentManagerRefSubSystem
{
  public static class MyComponent
  {
  }

  @ComponentManagerRef
  protected abstract ComponentManager<MyComponent> cm();

  @Processor
  final void runFrame()
  {
  }
}
