package com.example.stage;

import galdr.Stage;
import galdr.World;
import galdr.Worlds;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("galdr.processor.ApplicationProcessor")
final class Galdr_NonStandardMethodNameStage extends NonStandardMethodNameStage {
  @Nonnull
  private final Stage _$4v3;

  Galdr_NonStandardMethodNameStage() {
    final World world = Worlds
        .world()
        .stage( "$4v3" )
        .endStage()
        .build();
    _$4v3 = world.getStageByName( "$4v3" );
  }

  @Override
  @Nonnull
  Stage $4v3() {
    return _$4v3;
  }
}
