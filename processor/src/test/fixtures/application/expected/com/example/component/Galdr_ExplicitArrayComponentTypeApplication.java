package com.example.component;

import galdr.Stage;
import galdr.World;
import galdr.Worlds;
import javax.annotation.Nonnull;
import javax.annotation.processing.Generated;

@Generated("galdr.processor.ApplicationProcessor")
final class Galdr_ExplicitArrayComponentTypeApplication extends ExplicitArrayComponentTypeApplication {
  @Nonnull
  private final Stage _sim;

  Galdr_ExplicitArrayComponentTypeApplication() {
    final World world = Worlds
        .world()
        .component( ExplicitArrayComponentTypeApplication.MyComponent.class, ExplicitArrayComponentTypeApplication.MyComponent::new )
        .stage( "sim" )
        .subSystem( new ExplicitArrayComponentTypeApplication_Galdr_MySubSystem() )
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
