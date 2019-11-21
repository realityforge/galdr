package com.example.name_ref;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.NameRef;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class ParameterizedNameRefSubSystem
{
  @NameRef
  abstract String name( int i );

  @Processor
  final void runFrame()
  {
  }
}
