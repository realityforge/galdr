package com.example.component_manager_ref;

import galdr.ComponentManager;
import galdr.annotations.ComponentManagerRef;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;
import java.io.IOException;

@GaldrSubSystem
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
