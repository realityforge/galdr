package com.example.component;

import galdr.Stage;
import galdr.World;
import galdr.Worlds;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("galdr.processor.ApplicationProcessor")
final class Galdr_ExplicitFlagComponentTypeApplication extends ExplicitFlagComponentTypeApplication {
  @Nonnull
  private final Stage _sim;

  Galdr_ExplicitFlagComponentTypeApplication() {
    final World world = Worlds
        .world()
        .component( ExplicitFlagComponentTypeApplication.MyComponent.class )
        .stage( "sim" )
        .subSystem( new ExplicitFlagComponentTypeApplication_Galdr_MySubSystem() )
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
