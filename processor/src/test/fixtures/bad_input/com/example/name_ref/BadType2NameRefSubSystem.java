package com.example.name_ref;

import galdr.annotations.NameRef;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class BadType2NameRefSubSystem
{
  @NameRef
  abstract <T> T name();

  @Processor
  final void runFrame()
  {
  }
}
