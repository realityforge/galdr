package com.example.entity_processor.other;

import com.example.entity_processor.StaticEntityProcessorSubSystem;
import galdr.annotations.EntityProcessor;
import java.io.IOException;

public abstract class BaseUnreachableEntityProcessorSubSystem
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
