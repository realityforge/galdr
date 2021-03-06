package com.example.component_manager_ref;

import galdr.ComponentManager;
import galdr.annotations.ComponentManagerRef;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;

@GaldrSubSystem
abstract class MultiSameComponentManagerRefSubSystem
{
  @SuppressWarnings( "WeakerAccess" )
  public static class MyComponent1
  {
  }

  @ComponentManagerRef
  abstract ComponentManager<MyComponent1> cm1();

  @ComponentManagerRef
  abstract ComponentManager<MyComponent1> cm2();

  @Processor
  final void runFrame()
  {
  }
}
