package com.example.world_ref;

import galdr.World;
import galdr.annotations.WorldRef;

public interface WorldRefInterface
{
  @WorldRef
  World world();
}
