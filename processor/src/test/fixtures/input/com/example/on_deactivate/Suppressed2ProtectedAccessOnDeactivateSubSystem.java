package com.example.on_deactivate;

import galdr.annotations.OnDeactivate;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;
import galdr.annotations.SuppressGaldrWarnings;

@SubSystem
public abstract class Suppressed2ProtectedAccessOnDeactivateSubSystem
{
  // This uses the CLASS retention suppression
  @SuppressGaldrWarnings( "Galdr:ProtectedLifecycleMethod" )
  @OnDeactivate
  protected void onDeactivate()
  {
  }

  @Processor
  final void runFrame()
  {
  }
}
