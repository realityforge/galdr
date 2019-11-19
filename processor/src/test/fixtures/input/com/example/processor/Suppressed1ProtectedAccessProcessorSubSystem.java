package com.example.processor;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class Suppressed1ProtectedAccessProcessorSubSystem
{
  // This uses the SOURCE retention suppression
  @SuppressWarnings( "Galdr:ProtectedLifecycleMethod" )
  @Processor
  protected void runFrame()
  {
  }
}
