package com.example.component_manager_ref.other;

import galdr.ComponentManager;
import galdr.annotations.ComponentManagerRef;

public abstract class BaseUnreachableComponentManagerRefSubSystem
{
  @ComponentManagerRef
  abstract ComponentManager<String> cm();
}
