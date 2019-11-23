package com.example.component;

import galdr.Stage;
import galdr.World;
import galdr.Worlds;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("galdr.processor.ApplicationProcessor")
final class Galdr_AutodetectFlagComponentTypeApplication extends AutodetectFlagComponentTypeApplication {
  @Nonnull
  private final Stage _sim;

  Galdr_AutodetectFlagComponentTypeApplication() {
    final World world = Worlds
        .world()
        .component( AutodetectFlagComponentTypeApplication.MyComponent.class )
        .stage( "sim" )
        .subSystem( new AutodetectFlagComponentTypeApplication_Galdr_MySubSystem() )
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
