package com.example.entity_processor;

import galdr.annotations.EntityProcessor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class Suppressed1PublicAccessEntityProcessorSubSystem
{
  public static class MyComponent
  {
  }

  // This uses the SOURCE retention suppression
  @SuppressWarnings( "Galdr:PublicLifecycleMethod" )
  @EntityProcessor( all = MyComponent.class )
  public void process( int entityId )
  {
  }
}
