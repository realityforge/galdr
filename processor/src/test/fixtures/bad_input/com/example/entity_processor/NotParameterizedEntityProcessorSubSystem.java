package com.example.entity_processor;

import galdr.annotations.EntityProcessor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class NotParameterizedEntityProcessorSubSystem
{
  public static class MyComponent
  {
  }

  @EntityProcessor( all = MyComponent.class )
  void process()
  {
  }
}
