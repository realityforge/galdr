package com.example.entity_processor;

import galdr.annotations.EntityProcessor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class BasicEntityProcessorSubSystem
{
  public static class MyComponent
  {
  }

  @EntityProcessor( all = MyComponent.class )
  void process( int entityId )
  {
  }
}
