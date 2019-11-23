package com.example;

import com.example.other.CompleteInterfaceApplication;
import galdr.annotations.GaldrApplication;

@GaldrApplication( components = CompleteInterfaceApplication.MyComponent.class )
abstract class CompleteViaInterfaceApplication
  implements CompleteInterfaceApplication
{
}
