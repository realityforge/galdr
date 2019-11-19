package com.example.entity_processor;

import galdr.annotations.EntityProcessor;
import galdr.annotations.GaldrSubSystem;

@GaldrSubSystem
public abstract class OverlapExcludeExcludeEntityProcessorSubSystem
{
  public static class MyComponent
  {
  }

  @EntityProcessor( exclude = { MyComponent.class, MyComponent.class } )
  void process( int entityId )
  {
  }
}
