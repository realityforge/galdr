package com.example.stage;

import galdr.Stage;
import galdr.World;
import galdr.Worlds;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("galdr.processor.ApplicationProcessor")
final class Galdr_CustomNameStage extends CustomNameStage {
  @Nonnull
  private final Stage _monkey;

  Galdr_CustomNameStage() {
    final World world = Worlds
        .world()
        .stage( "monkey" )
        .subSystem( new CustomNameStage_Galdr_MySubSystem() )
        .endStage()
        .build();
    _monkey = world.getStageByName( "monkey" );
  }

  @Override
  @Nonnull
  Stage sim() {
    return _monkey;
  }
}
