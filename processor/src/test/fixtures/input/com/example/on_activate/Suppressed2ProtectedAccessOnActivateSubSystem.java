package com.example.on_activate;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.OnActivate;
import galdr.annotations.Processor;
import galdr.annotations.SuppressGaldrWarnings;

@GaldrSubSystem
public abstract class Suppressed2ProtectedAccessOnActivateSubSystem
{
  // This uses the CLASS retention suppression
  @SuppressGaldrWarnings( "Galdr:ProtectedLifecycleMethod" )
  @OnActivate
  protected void onActivate()
  {
  }

  @Processor
  final void runFrame()
  {
  }
}
