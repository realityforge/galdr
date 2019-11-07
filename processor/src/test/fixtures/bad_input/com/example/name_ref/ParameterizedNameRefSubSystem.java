package com.example.name_ref;

import galdr.annotations.NameRef;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class ParameterizedNameRefSubSystem
{
  @NameRef
  abstract String name( int i );

  @Processor
  final void runFrame()
  {
  }
}
