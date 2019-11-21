package com.example;

import galdr.Stage;
import galdr.World;
import galdr.Worlds;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("galdr.processor.ApplicationProcessor")
final class Galdr_CompleteViaInterfaceApplication extends CompleteViaInterfaceApplication {
  @Nonnull
  private final Stage _sim;

  @Nonnull
  private final Stage _render;

  Galdr_CompleteViaInterfaceApplication() {
    final World world = Worlds
        .world()
        .stage( "sim" )
        .endStage()
        .stage( "render" )
        .endStage()
        .build();
    _sim = world.getStageByName( "sim" );
    _render = world.getStageByName( "render" );
  }

  @Override
  @Nonnull
  public Stage sim() {
    return _sim;
  }

  @Override
  @Nonnull
  public Stage render() {
    return _render;
  }
}
