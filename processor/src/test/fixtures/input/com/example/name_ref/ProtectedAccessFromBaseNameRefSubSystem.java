package com.example.name_ref;

import com.example.name_ref.other.BaseProtectedAccessNameRefSubSystem;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class ProtectedAccessFromBaseNameRefSubSystem
  extends BaseProtectedAccessNameRefSubSystem
{
  @Processor
  final void runFrame()
  {
  }
}
