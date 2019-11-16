package com.example.on_deactivate.other;

import galdr.annotations.OnDeactivate;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

public abstract class BaseProtectedAccessOnDeactivateSubSystem
{
  @OnDeactivate
  protected void onDeactivate()
  {
  }
}
