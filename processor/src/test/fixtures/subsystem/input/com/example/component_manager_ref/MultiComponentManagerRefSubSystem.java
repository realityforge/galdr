package com.example.component_manager_ref;

import galdr.ComponentManager;
import galdr.annotations.ComponentManagerRef;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.Processor;

@GaldrSubSystem
abstract class MultiComponentManagerRefSubSystem
{
  static class MyComponent1
  {
  }

  static class MyComponent2
  {
  }

  @ComponentManagerRef
  abstract ComponentManager<MyComponent1> cm1();

  @ComponentManagerRef
  abstract ComponentManager<MyComponent2> cm2();

  @Processor
  final void runFrame()
  {
  }
}
