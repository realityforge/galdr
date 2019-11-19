package com.example.entity_processor;

import galdr.annotations.EntityProcessor;
import galdr.annotations.GaldrSubSystem;

@GaldrSubSystem
public abstract class PrivateEntityProcessorSubSystem
{
  public static class MyComponent
  {
  }

  @EntityProcessor( all = MyComponent.class )
  private void process( int entityId )
  {
  }
}
