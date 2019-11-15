package com.example.component_manager_ref;

import galdr.ComponentManager;

public interface ComponentManagerRefInterface
{
  class MyComponent
  {
  }

  ComponentManager<MyComponent> cm();
}
