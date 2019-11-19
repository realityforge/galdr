package com.example.name_ref;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.NameRef;
import galdr.annotations.Processor;

@GaldrSubSystem
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
