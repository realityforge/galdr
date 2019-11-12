package com.example.component_manager_ref;

import galdr.ComponentManager;
import galdr.annotations.ComponentManagerRef;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SuppressWarnings( "WeakerAccess" )
@SubSystem
public abstract class PublicAccessComponentManagerRefSubSystem
{
  public static class MyComponent
  {
  }

  @ComponentManagerRef
  public abstract ComponentManager<MyComponent> cm();

  @Processor
  final void runFrame()
  {
  }
}
