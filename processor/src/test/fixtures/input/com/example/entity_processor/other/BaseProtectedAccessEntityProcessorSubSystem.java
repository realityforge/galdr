package com.example.entity_processor.other;

import galdr.annotations.EntityProcessor;

public abstract class BaseProtectedAccessEntityProcessorSubSystem
{
  public static class MyComponent
  {
  }

  @EntityProcessor( all = MyComponent.class )
  protected void process( int entityId )
  {
  }
}
