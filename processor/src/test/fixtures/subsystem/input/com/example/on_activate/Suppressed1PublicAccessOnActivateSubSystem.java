package com.example.on_activate;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.OnActivate;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class Suppressed1PublicAccessOnActivateSubSystem
{
  // This uses the SOURCE retention suppression
  @SuppressWarnings( "Galdr:PublicLifecycleMethod" )
  @OnActivate
  public void onActivate()
  {
  }

  @Processor
  final void runFrame()
  {
  }
}
