package com.example.name_ref;

import com.example.name_ref.other.BaseUnreachableNameRefSubSystem;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;

@GaldrSubSystem
public abstract class UnreachableNameRefSubSystem
  extends BaseUnreachableNameRefSubSystem
{
  @Processor
  final void runFrame()
  {
  }
}
