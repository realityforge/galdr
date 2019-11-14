package com.example.on_activate;

import com.example.on_activate.other.BaseUnreachableOnActivateSubSystem;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class UnreachableOnActivateSubSystem
  extends BaseUnreachableOnActivateSubSystem
{
  @Processor
  final void runFrame()
  {
  }
}
