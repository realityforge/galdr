package com.example.name_ref;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.NameRef;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class BadType2NameRefSubSystem
{
  @NameRef
  abstract <T> T name();

  @Processor
  final void runFrame()
  {
  }
}
