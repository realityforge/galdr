package com.example.component_manager_ref;

import galdr.ComponentManager;
import galdr.annotations.ComponentManagerRef;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class PublicAccessViaInterfaceComponentManagerRefSubSystem
  implements ComponentManagerRefInterface
{
  @Override
  @ComponentManagerRef
  public abstract ComponentManager<MyComponent> cm();

  @Processor
  final void runFrame()
  {
  }
}
