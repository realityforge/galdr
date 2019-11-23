package com.example;

import galdr.Stage;
import galdr.World;
import galdr.Worlds;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("galdr.processor.ApplicationProcessor")
final class Galdr_PackageAccessApplication extends PackageAccessApplication {
  @Nonnull
  private final Stage _sim;

  Galdr_PackageAccessApplication() {
    final World world = Worlds
        .world()
        .stage( "sim" )
        .subSystem( new PackageAccessApplication_Galdr_MySubSystem() )
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
