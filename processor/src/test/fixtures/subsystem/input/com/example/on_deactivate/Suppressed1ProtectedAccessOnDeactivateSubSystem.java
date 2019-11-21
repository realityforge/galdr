package com.example.on_deactivate;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.OnDeactivate;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class Suppressed1ProtectedAccessOnDeactivateSubSystem
{
  // This uses the SOURCE retention suppression
  @SuppressWarnings( "Galdr:ProtectedLifecycleMethod" )
  @OnDeactivate
  protected void onDeactivate()
  {
  }

  @Processor
  final void runFrame()
  {
  }
}
