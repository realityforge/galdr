package com.example.entity_processor;

import galdr.annotations.EntityProcessor;
import galdr.annotations.GaldrSubSystem;
import java.io.IOException;

@GaldrSubSystem
public abstract class ThrowsExceptionEntityProcessorSubSystem
{
  public static class MyComponent
  {
  }

  @EntityProcessor( all = MyComponent.class )
  void process( int entityId )
    throws IOException
  {
  }
}
