package com.example.entity_processor;

import galdr.annotations.EntityProcessor;
import galdr.annotations.GaldrSubSystem;

@GaldrSubSystem
public abstract class MultiEntityProcessorSubSystem
{
  public static class MyComponent1
  {
  }

  public static class MyComponent2
  {
  }

  @EntityProcessor( all = MyComponent1.class )
  void process1( int entityId )
  {
  }

  @EntityProcessor( all = MyComponent2.class )
  void process2( int entityId )
  {
  }
}
