package com.example.name_ref;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.NameRef;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class PublicAccessViaInterfaceNameRefSubSystem
  implements NameRefInterface
{
  @Override
  @NameRef
  public abstract String name();

  @Processor
  final void runFrame()
  {
  }
}
