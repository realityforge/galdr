package com.example.component;

import galdr.Stage;
import galdr.World;
import galdr.Worlds;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("galdr.processor.ApplicationProcessor")
final class Galdr_ExplicitAutodetectComponentTypeApplication extends ExplicitAutodetectComponentTypeApplication {
  @Nonnull
  private final Stage _sim;

  Galdr_ExplicitAutodetectComponentTypeApplication() {
    final World world = Worlds
        .world()
        .component( ExplicitAutodetectComponentTypeApplication.MyComponent.class, ExplicitAutodetectComponentTypeApplication.MyComponent::new )
        .stage( "sim" )
        .subSystem( new ExplicitAutodetectComponentTypeApplication_Galdr_MySubSystem() )
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
