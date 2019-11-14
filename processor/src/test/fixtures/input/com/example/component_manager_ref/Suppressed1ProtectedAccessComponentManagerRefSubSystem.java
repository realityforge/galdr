package com.example.component_manager_ref;

import galdr.ComponentManager;
import galdr.annotations.ComponentManagerRef;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class Suppressed1ProtectedAccessComponentManagerRefSubSystem
{
  @SuppressWarnings( "WeakerAccess" )
  public static class MyComponent
  {
  }

  // This uses the SOURCE retention suppression
  @SuppressWarnings( "Galdr:ProtectedRefMethod" )
  @ComponentManagerRef
  protected abstract ComponentManager<MyComponent> cm();

  @Processor
  final void runFrame()
  {
  }
}
