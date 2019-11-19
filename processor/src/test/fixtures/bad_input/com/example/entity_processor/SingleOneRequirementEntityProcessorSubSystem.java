package com.example.entity_processor;

import galdr.annotations.EntityProcessor;
import galdr.annotations.GaldrSubSystem;

@GaldrSubSystem
public abstract class SingleOneRequirementEntityProcessorSubSystem
{
  public static class MyComponent
  {
  }

  @EntityProcessor( one = MyComponent.class )
  void process( int entityId )
  {
  }
}
