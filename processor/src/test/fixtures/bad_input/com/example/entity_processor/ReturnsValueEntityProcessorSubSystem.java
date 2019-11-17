package com.example.entity_processor;

import galdr.annotations.EntityProcessor;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class ReturnsValueEntityProcessorSubSystem
{
  public static class MyComponent
  {
  }

  @EntityProcessor( all = MyComponent.class )
  String process( int entityId )
  {
    return null;
  }
}
