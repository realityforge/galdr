package com.example.entity_processor;

import galdr.annotations.EntityProcessor;
import galdr.annotations.GaldrSubSystem;

@GaldrSubSystem
public abstract class OverlapAllAllEntityProcessorSubSystem
{
  public static class MyComponent
  {
  }

  @EntityProcessor( all = { MyComponent.class, MyComponent.class } )
  void process( int entityId )
  {
  }
}
