package com.example.name_ref;

import galdr.annotations.NameRef;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
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
