package com.example.entity_processor;

import galdr.annotations.EntityProcessor;
import galdr.annotations.GaldrSubSystem;

@GaldrSubSystem
public abstract class OverlapOneOneEntityProcessorSubSystem
{
  public static class MyComponent
  {
  }

  @EntityProcessor( one = { MyComponent.class, MyComponent.class } )
  void process( int entityId )
  {
  }
}
