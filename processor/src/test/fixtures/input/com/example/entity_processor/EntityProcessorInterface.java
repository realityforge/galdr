package com.example.entity_processor;

import galdr.annotations.EntityProcessor;

public interface EntityProcessorInterface
{
  class MyComponent
  {
  }

  @EntityProcessor( all = MyComponent.class )
  default void process( int entityId )
  {
  }
}
