package com.example.processor;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;
import galdr.annotations.SuppressGaldrWarnings;

@GaldrSubSystem
public abstract class Suppressed2ProtectedAccessProcessorSubSystem
{
  // This uses the CLASS retention suppression
  @SuppressGaldrWarnings( "Galdr:ProtectedLifecycleMethod" )
  @Processor
  protected void runFrame()
  {
  }
}
