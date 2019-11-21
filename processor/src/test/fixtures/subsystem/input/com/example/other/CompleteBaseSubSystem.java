package com.example.other;

import galdr.ComponentManager;
import galdr.World;
import galdr.annotations.ComponentManagerRef;
import galdr.annotations.NameRef;
import galdr.annotations.OnActivate;
import galdr.annotations.OnDeactivate;
import galdr.annotations.Processor;
import galdr.annotations.WorldRef;

public abstract class CompleteBaseSubSystem
{
  public static class MyComponent1
  {
  }

  public static class MyComponent2
  {
  }

  @ComponentManagerRef
  protected abstract ComponentManager<MyComponent1> cm1();

  @ComponentManagerRef
  protected abstract ComponentManager<MyComponent2> cm2();

  @NameRef
  protected abstract String name1();

  @NameRef
  protected abstract String name2();

  @WorldRef
  protected abstract World world1();

  @WorldRef
  protected abstract World world2();

  @OnActivate
  protected void onActivate1()
  {
  }

  @OnActivate
  protected void onActivate2()
  {
  }

  @OnDeactivate
  protected void onDeactivate1()
  {
  }

  @OnDeactivate
  protected void onDeactivate2()
  {
  }

  @Processor
  protected void runFrame1()
  {
  }

  @Processor
  protected void runFrame2()
  {
  }
}
