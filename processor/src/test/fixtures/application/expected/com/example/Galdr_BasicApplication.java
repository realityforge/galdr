package com.example;

import galdr.Stage;
import galdr.World;
import galdr.Worlds;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("galdr.processor.ApplicationProcessor")
final class Galdr_BasicApplication extends BasicApplication {
  @Nonnull
  private final Stage _sim;

  Galdr_BasicApplication() {
    final World world = Worlds
        .world()
        .component( BasicApplication.MyComponent.class )
        .stage( "sim" )
        .subSystem( new BasicApplication_Galdr_MySubSystem() )
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
