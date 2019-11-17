package com.example.entity_processor;

import galdr.annotations.EntityProcessor;
import galdr.annotations.SubSystem;

@SubSystem
public abstract class ComplexAreaOfInterest1EntityProcessorSubSystem
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

  public static class MyComponent4
  {
  }

  public static class MyComponent5
  {
  }

  public static class MyComponent6
  {
  }

  @EntityProcessor(
    all = { MyComponent1.class, MyComponent2.class, MyComponent3.class },
    one = { MyComponent4.class, MyComponent5.class },
    exclude = MyComponent6.class )
  void process( int entityId )
  {
  }
}
