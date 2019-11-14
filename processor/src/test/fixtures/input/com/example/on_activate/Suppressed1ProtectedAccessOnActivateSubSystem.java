package com.example.on_activate;

import galdr.annotations.OnActivate;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class Suppressed1ProtectedAccessOnActivateSubSystem
{
  // This uses the SOURCE retention suppression
  @SuppressWarnings( "Galdr:ProtectedLifecycleMethod" )
  @OnActivate
  protected void onActivate()
  {
  }

  @Processor
  final void runFrame()
  {
  }
}
