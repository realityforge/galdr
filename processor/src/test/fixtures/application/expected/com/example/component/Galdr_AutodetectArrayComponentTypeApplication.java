package com.example.component;

import galdr.Stage;
import galdr.World;
import galdr.Worlds;
import javax.annotation.Nonnull;
import javax.annotation.processing.Generated;

@Generated("galdr.processor.ApplicationProcessor")
final class Galdr_AutodetectArrayComponentTypeApplication extends AutodetectArrayComponentTypeApplication {
  @Nonnull
  private final Stage _sim;

  Galdr_AutodetectArrayComponentTypeApplication() {
    final World world = Worlds
        .world()
        .component( AutodetectArrayComponentTypeApplication.MyComponent.class, AutodetectArrayComponentTypeApplication.MyComponent::new )
        .stage( "sim" )
        .subSystem( new AutodetectArrayComponentTypeApplication_Galdr_MySubSystem() )
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
