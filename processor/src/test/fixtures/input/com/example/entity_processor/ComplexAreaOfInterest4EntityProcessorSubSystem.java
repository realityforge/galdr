package com.example.entity_processor;

import galdr.annotations.EntityProcessor;
import galdr.annotations.GaldrSubSystem;

@GaldrSubSystem
public abstract class ComplexAreaOfInterest4EntityProcessorSubSystem
{
  public static class MyComponent1
  {
  }

  public static class MyComponent2
  {
  }

  public static class MyComponent3
  {
  }

  @EntityProcessor( exclude = MyComponent1.class, one = { MyComponent2.class, MyComponent3.class } )
  void process( int entityId )
  {
  }
}