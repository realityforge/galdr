package com.example;

import galdr.Stage;
import galdr.World;
import galdr.Worlds;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("galdr.processor.ApplicationProcessor")
final class StaticInnerClassApplication_Galdr_BasicApplication extends StaticInnerClassApplication.BasicApplication {
  @Nonnull
  private final Stage _sim;

  StaticInnerClassApplication_Galdr_BasicApplication() {
    final World world = Worlds
        .world()
        .stage( "sim" )
        .endStage()
        .build();
    _sim = world.getStageByName( "sim" );
  }

  @Override
  @Nonnull
  Stage sim() {
    return _sim;
  }
}
