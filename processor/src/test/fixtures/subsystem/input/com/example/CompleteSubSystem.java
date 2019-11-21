package com.example;

import galdr.ComponentManager;
import galdr.World;
import galdr.annotations.ComponentManagerRef;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.NameRef;
import galdr.annotations.OnActivate;
import galdr.annotations.OnDeactivate;
import galdr.annotations.Processor;
import galdr.annotations.WorldRef;

@GaldrSubSystem
abstract class CompleteSubSystem
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

  @NameRef
  abstract String name1();

  @NameRef
  abstract String name2();

  @WorldRef
  abstract World world1();

  @WorldRef
  abstract World world2();

  @OnActivate
  void onActivate1()
  {
  }

  @OnActivate
  void onActivate2()
  {
  }

  @OnDeactivate
  void onDeactivate1()
  {
  }

  @OnDeactivate
  void onDeactivate2()
  {
  }

  @Processor
  final void runFrame1()
  {
  }

  @Processor
  final void runFrame2()
  {
  }
}
