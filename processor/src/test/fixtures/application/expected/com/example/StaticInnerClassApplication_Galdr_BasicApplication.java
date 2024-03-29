package com.example;

import galdr.Stage;
import galdr.World;
import galdr.Worlds;
import javax.annotation.Nonnull;
import javax.annotation.processing.Generated;

@Generated("galdr.processor.ApplicationProcessor")
final class StaticInnerClassApplication_Galdr_BasicApplication extends StaticInnerClassApplication.BasicApplication {
  @Nonnull
  private final Stage _sim;

  StaticInnerClassApplication_Galdr_BasicApplication() {
    final World world = Worlds
        .world()
        .component( StaticInnerClassApplication.MyComponent.class )
        .stage( "sim" )
        .subSystem( new StaticInnerClassApplication_BasicApplication_Galdr_MySubSystem() )
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
