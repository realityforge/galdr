package com.example.entity_processor;

import galdr.annotations.EntityProcessor;
import galdr.annotations.SubSystem;

@SubSystem
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
