package com.example.entity_processor;

import galdr.annotations.EntityProcessor;
import galdr.annotations.SubSystem;
import galdr.annotations.SuppressGaldrWarnings;

@SubSystem
public abstract class Suppressed2PublicAccessEntityProcessorSubSystem
{
  public static class MyComponent
  {
  }

  // This uses the CLASS retention suppression
  @SuppressGaldrWarnings( "Galdr:PublicLifecycleMethod" )
  @EntityProcessor( all = MyComponent.class )
  public void process( int entityId )
  {
  }
}
