package com.example.entity_processor;

import galdr.annotations.EntityProcessor;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;
import java.io.IOException;

@SubSystem
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
