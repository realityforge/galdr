package com.example.on_deactivate;

import com.example.on_deactivate.other.BaseUnreachableOnDeactivateSubSystem;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class UnreachableOnDeactivateSubSystem
  extends BaseUnreachableOnDeactivateSubSystem
{
  @Processor
  final void runFrame()
  {
  }
}
