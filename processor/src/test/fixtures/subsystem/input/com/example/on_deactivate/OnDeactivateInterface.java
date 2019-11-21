package com.example.on_deactivate;

import galdr.annotations.OnDeactivate;

public interface OnDeactivateInterface
{
  @OnDeactivate
  default void onDeactivate()
  {
  }
}
