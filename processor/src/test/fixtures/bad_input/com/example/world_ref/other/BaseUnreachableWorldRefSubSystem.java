package com.example.world_ref.other;

import galdr.World;
import galdr.annotations.WorldRef;

public abstract class BaseUnreachableWorldRefSubSystem
{
  @WorldRef
  abstract World world();
}
