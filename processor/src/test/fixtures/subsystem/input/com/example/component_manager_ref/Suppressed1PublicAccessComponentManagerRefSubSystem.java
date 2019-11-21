package com.example.component_manager_ref;

import galdr.ComponentManager;
import galdr.annotations.ComponentManagerRef;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class Suppressed1PublicAccessComponentManagerRefSubSystem
{
  public static class MyComponent
  {
  }

  // This uses the SOURCE retention suppression
  @SuppressWarnings( "Galdr:PublicRefMethod" )
  @ComponentManagerRef
  public abstract ComponentManager<MyComponent> cm();

  @Processor
  final void runFrame()
  {
  }
}
