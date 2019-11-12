package com.example.name_ref;

import galdr.annotations.NameRef;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;
import java.io.IOException;

@SubSystem
public abstract class BadType3NameRefSubSystem
{
  @NameRef
  abstract IOException name();

  @Processor
  final void runFrame()
  {
  }
}
