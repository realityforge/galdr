package com.example.on_deactivate;

import com.example.on_deactivate.other.BaseUnreachableOnDeactivateSubSystem;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class UnreachableOnDeactivateSubSystem
  extends BaseUnreachableOnDeactivateSubSystem
{
  @Processor
  final void runFrame()
  {
  }
}
