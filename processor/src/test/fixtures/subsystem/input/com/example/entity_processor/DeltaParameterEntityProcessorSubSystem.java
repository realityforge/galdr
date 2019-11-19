package com.example.entity_processor;

import galdr.annotations.EntityProcessor;
import galdr.annotations.GaldrSubSystem;

@GaldrSubSystem
public abstract class DeltaParameterEntityProcessorSubSystem
{
  public static class MyComponent
  {
  }

  @EntityProcessor( all = MyComponent.class )
  void process( int delta, int entityId )
  {
  }
}
