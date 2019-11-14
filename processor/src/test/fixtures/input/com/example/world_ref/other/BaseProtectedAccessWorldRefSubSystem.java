package com.example.world_ref.other;

import galdr.World;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;
import galdr.annotations.WorldRef;

public abstract class BaseProtectedAccessWorldRefSubSystem
{
  @WorldRef
  protected abstract World world();
}
