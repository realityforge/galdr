package com.example.processor;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class Suppressed1PublicAccessProcessorSubSystem
{
  // This uses the SOURCE retention suppression
  @SuppressWarnings( "Galdr:PublicLifecycleMethod" )
  @Processor
  public void runFrame()
  {
  }
}
