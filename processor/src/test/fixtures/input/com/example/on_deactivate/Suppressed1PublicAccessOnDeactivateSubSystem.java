package com.example.on_deactivate;

import galdr.annotations.OnDeactivate;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class Suppressed1PublicAccessOnDeactivateSubSystem
{
  // This uses the SOURCE retention suppression
  @SuppressWarnings( "Galdr:PublicLifecycleMethod" )
  @OnDeactivate
  public void onDeactivate()
  {
  }

  @Processor
  final void runFrame()
  {
  }
}
