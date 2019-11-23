package com.example;

import com.example.other.CompleteBaseApplication;
import galdr.annotations.GaldrApplication;

@GaldrApplication( components = CompleteBaseApplication.MyComponent.class )
abstract class CompleteViaBaseClassApplication
  extends CompleteBaseApplication
{
}
