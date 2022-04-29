package com.example.component;

import galdr.ComponentStorage;
import galdr.Stage;
import galdr.World;
import galdr.Worlds;
import javax.annotation.Nonnull;
import javax.annotation.processing.Generated;

@Generated("galdr.processor.ApplicationProcessor")
final class Galdr_ExplicitMapComponentTypeApplication extends ExplicitMapComponentTypeApplication {
  @Nonnull
  private final Stage _sim;

  Galdr_ExplicitMapComponentTypeApplication() {
    final World world = Worlds
        .world()
        .component( ExplicitMapComponentTypeApplication.MyComponent.class, ExplicitMapComponentTypeApplication.MyComponent::new, ComponentStorage.MAP )
        .stage( "sim" )
        .subSystem( new ExplicitMapComponentTypeApplication_Galdr_MySubSystem() )
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
