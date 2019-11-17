package com.example.entity_processor;

import galdr.annotations.EntityProcessor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class NoAreaOfInterestEntityProcessorSubSystem
{
  public static class MyComponent
  {
  }

  @EntityProcessor
  void process( int entityId )
  {
  }
}
