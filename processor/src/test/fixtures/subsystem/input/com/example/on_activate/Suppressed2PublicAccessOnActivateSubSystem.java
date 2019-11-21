package com.example.on_activate;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.OnActivate;
import galdr.annotations.Processor;
import galdr.annotations.SuppressGaldrWarnings;

@GaldrSubSystem
public abstract class Suppressed2PublicAccessOnActivateSubSystem
{
  // This uses the CLASS retention suppression
  @SuppressGaldrWarnings( "Galdr:PublicLifecycleMethod" )
  @OnActivate
  public void onActivate()
  {
  }

  @Processor
  final void runFrame()
  {
  }
}
