package com.example.on_activate;

import galdr.annotations.OnActivate;

public interface OnActivateInterface
{
  @OnActivate
  default void onActivate()
  {
  }
}
