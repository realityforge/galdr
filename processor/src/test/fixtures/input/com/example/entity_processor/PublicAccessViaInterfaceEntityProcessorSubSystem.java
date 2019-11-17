package com.example.entity_processor;

import galdr.annotations.EntityProcessor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class PublicAccessViaInterfaceEntityProcessorSubSystem
  implements EntityProcessorInterface
{
  public static class MyComponent
  {
  }

  @Override
  @EntityProcessor( all = MyComponent.class )
  public void process( int entityId )
  {
  }
}
