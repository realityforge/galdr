package com.example.component_manager_ref.other;

import galdr.ComponentManager;
import galdr.annotations.ComponentManagerRef;

public abstract class BaseProtectedAccessComponentManagerRefSubSystem
{
  public static class MyComponent
  {
  }

  @ComponentManagerRef
  protected abstract ComponentManager<MyComponent> cm();
}
