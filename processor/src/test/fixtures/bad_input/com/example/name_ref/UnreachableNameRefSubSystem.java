package com.example.name_ref;

import com.example.name_ref.other.BaseUnreachableNameRefSubSystem;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class UnreachableNameRefSubSystem
  extends BaseUnreachableNameRefSubSystem
{
  @Processor
  final void runFrame()
  {
  }
}
