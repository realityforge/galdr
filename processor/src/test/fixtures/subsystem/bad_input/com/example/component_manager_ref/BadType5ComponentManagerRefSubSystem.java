package com.example.component_manager_ref;

import galdr.ComponentManager;
import galdr.annotations.ComponentManagerRef;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class BadType5ComponentManagerRefSubSystem
{
  @ComponentManagerRef
  abstract <T> ComponentManager<T> cm();

  @Processor
  final void runFrame()
  {
  }
}
