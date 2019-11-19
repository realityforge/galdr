package com.example.entity_processor;

import galdr.annotations.EntityProcessor;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.SuppressGaldrWarnings;

@GaldrSubSystem
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
