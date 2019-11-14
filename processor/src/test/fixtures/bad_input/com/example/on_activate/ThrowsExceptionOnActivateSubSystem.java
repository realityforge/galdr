package com.example.on_activate;

import galdr.annotations.OnActivate;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;
import java.io.IOException;

@SubSystem
public abstract class ThrowsExceptionOnActivateSubSystem
{
  @OnActivate
  final void onActivate()
    throws IOException
  {
  }

  @Processor
  final void runFrame()
  {
  }
}
