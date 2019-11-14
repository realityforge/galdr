package com.example.name_ref;

import galdr.annotations.NameRef;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class Suppressed1PublicAccessNameRefSubSystem
{
  // This uses the SOURCE retention suppression
  @SuppressWarnings( "Galdr:PublicRefMethod" )
  @NameRef
  public abstract String name();

  @Processor
  final void runFrame()
  {
  }
}
