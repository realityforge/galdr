package com.example.component_manager_ref;

import galdr.ComponentManager;
import galdr.annotations.ComponentManagerRef;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;
import galdr.annotations.SuppressGaldrWarnings;

@GaldrSubSystem
public abstract class Suppressed2ProtectedAccessComponentManagerRefSubSystem
{
  public static class MyComponent
  {
  }

  // This uses the CLASS retention suppression
  @SuppressGaldrWarnings( "Galdr:ProtectedRefMethod" )
  @ComponentManagerRef
  protected abstract ComponentManager<MyComponent> cm();

  @Processor
  final void runFrame()
  {
  }
}
