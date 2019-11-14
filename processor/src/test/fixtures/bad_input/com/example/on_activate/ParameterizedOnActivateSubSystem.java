package com.example.on_activate;

import galdr.annotations.OnActivate;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;
import java.io.IOException;

@SubSystem
public abstract class ParameterizedOnActivateSubSystem
{
  @OnActivate
  final void onActivate(int i)
  {
  }

  @Processor
  final void runFrame()
  {
  }
}
