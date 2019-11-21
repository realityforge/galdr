package com.example.processor;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;
import galdr.annotations.SuppressGaldrWarnings;

@GaldrSubSystem
public abstract class Suppressed2PublicAccessProcessorSubSystem
{
  // This uses the CLASS retention suppression
  @SuppressGaldrWarnings( "Galdr:PublicLifecycleMethod" )
  @Processor
  public void runFrame()
  {
  }
}
