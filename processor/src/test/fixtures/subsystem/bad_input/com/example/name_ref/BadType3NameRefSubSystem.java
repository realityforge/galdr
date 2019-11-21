package com.example.name_ref;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.NameRef;
import galdr.annotations.Processor;
import java.io.IOException;

@GaldrSubSystem
public abstract class BadType3NameRefSubSystem
{
  @NameRef
  abstract IOException name();

  @Processor
  final void runFrame()
  {
  }
}
