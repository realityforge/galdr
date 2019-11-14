package com.example.name_ref;

import com.example.name_ref.other.BaseProtectedAccessNameRefSubSystem;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class ProtectedAccessFromBaseNameRefSubSystem
  extends BaseProtectedAccessNameRefSubSystem
{
  @Processor
  final void runFrame()
  {
  }
}
