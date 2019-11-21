package com.example;

import galdr.Stage;
import galdr.World;
import galdr.Worlds;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("galdr.processor.ApplicationProcessor")
final class Galdr_CompleteViaBaseClassApplication extends CompleteViaBaseClassApplication {
  @Nonnull
  private final Stage _sim;

  @Nonnull
  private final Stage _render;

  Galdr_CompleteViaBaseClassApplication() {
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
  protected Stage sim() {
    return _sim;
  }

  @Override
  @Nonnull
  protected Stage render() {
    return _render;
  }
}
