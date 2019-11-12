package com.example.component_manager_ref;

import galdr.ComponentManager;
import galdr.annotations.ComponentManagerRef;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;
import java.io.IOException;

@SubSystem
public abstract class ThrowsExceptionComponentManagerRefSubSystem
{
  @ComponentManagerRef
  abstract ComponentManager<String> cm()
    throws IOException;

  @Processor
  final void runFrame()
  {
  }
}
