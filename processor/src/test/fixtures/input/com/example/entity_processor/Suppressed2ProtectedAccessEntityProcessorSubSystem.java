package com.example.entity_processor;

import galdr.annotations.EntityProcessor;
import galdr.annotations.SubSystem;
import galdr.annotations.SuppressGaldrWarnings;

@SubSystem
public abstract class Suppressed2ProtectedAccessEntityProcessorSubSystem
{
  public static class MyComponent
  {
  }

  // This uses the CLASS retention suppression
  @SuppressGaldrWarnings( "Galdr:ProtectedLifecycleMethod" )
  @EntityProcessor( all = MyComponent.class )
  protected void process( int entityId )
  {
  }
}
