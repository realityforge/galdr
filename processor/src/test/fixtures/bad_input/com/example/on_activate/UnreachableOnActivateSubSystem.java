package com.example.on_activate;

import com.example.on_activate.other.BaseUnreachableOnActivateSubSystem;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class UnreachableOnActivateSubSystem
  extends BaseUnreachableOnActivateSubSystem
{
  @Processor
  final void runFrame()
  {
  }
}
