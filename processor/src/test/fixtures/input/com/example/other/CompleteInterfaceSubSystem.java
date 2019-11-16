package com.example.other;

import galdr.ComponentManager;
import galdr.World;
import galdr.annotations.ComponentManagerRef;
import galdr.annotations.NameRef;
import galdr.annotations.OnActivate;
import galdr.annotations.OnDeactivate;
import galdr.annotations.Processor;
import galdr.annotations.WorldRef;

public interface CompleteInterfaceSubSystem
{
  class MyComponent1
  {
  }

  class MyComponent2
  {
  }

  @ComponentManagerRef
  ComponentManager<MyComponent1> cm1();

  @ComponentManagerRef
  ComponentManager<MyComponent2> cm2();

  @NameRef
  String name1();

  @NameRef
  String name2();

  @WorldRef
  World world1();

  @WorldRef
  World world2();

  @OnActivate
  default void onActivate1()
  {
  }

  @OnActivate
  default void onActivate2()
  {
  }

  @OnDeactivate
  default void onDeactivate1()
  {
  }

  @OnDeactivate
  default void onDeactivate2()
  {
  }

  @Processor
  default void runFrame()
  {
  }
}
