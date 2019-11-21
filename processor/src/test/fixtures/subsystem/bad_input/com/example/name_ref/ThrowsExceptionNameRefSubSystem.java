package com.example.name_ref;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.NameRef;
import galdr.annotations.Processor;
import java.io.IOException;

@GaldrSubSystem
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
