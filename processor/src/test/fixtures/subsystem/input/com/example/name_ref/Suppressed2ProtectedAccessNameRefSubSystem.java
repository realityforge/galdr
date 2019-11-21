package com.example.name_ref;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.NameRef;
import galdr.annotations.Processor;
import galdr.annotations.SuppressGaldrWarnings;

@GaldrSubSystem
public abstract class Suppressed2ProtectedAccessNameRefSubSystem
{
  // This uses the CLASS retention suppression
  @SuppressGaldrWarnings( "Galdr:ProtectedRefMethod" )
  @NameRef
  protected abstract String name();

  @Processor
  final void runFrame()
  {
  }
}
