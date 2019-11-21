package com.example.on_activate;

import galdr.annotations.GaldrSubSystem;
import galdr.annotations.OnActivate;
import galdr.annotations.Processor;
import java.io.IOException;

@GaldrSubSystem
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
