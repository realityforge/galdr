package com.example.entity_processor;

import galdr.annotations.EntityProcessor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class Suppressed1ProtectedAccessEntityProcessorSubSystem
{
  public static class MyComponent
  {
  }

  // This uses the SOURCE retention suppression
  @SuppressWarnings( "Galdr:ProtectedLifecycleMethod" )
  @EntityProcessor( all = MyComponent.class )
  protected void process( int entityId )
  {
  }
}
