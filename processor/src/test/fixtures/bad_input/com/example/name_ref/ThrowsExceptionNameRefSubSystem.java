package com.example.name_ref;

import galdr.annotations.NameRef;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;
import java.io.IOException;

@SubSystem
public abstract class ThrowsExceptionNameRefSubSystem
{
  @NameRef
  abstract String name()
    throws IOException;

  @Processor
  final void runFrame()
  {
  }
}
