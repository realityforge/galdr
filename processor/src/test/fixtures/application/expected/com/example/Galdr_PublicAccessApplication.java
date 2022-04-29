package com.example;

import galdr.Stage;
import galdr.World;
import galdr.Worlds;
import javax.annotation.Nonnull;
import javax.annotation.processing.Generated;

@Generated("galdr.processor.ApplicationProcessor")
final class Galdr_PublicAccessApplication extends PublicAccessApplication {
  @Nonnull
  private final Stage _sim;

  Galdr_PublicAccessApplication() {
    final World world = Worlds
        .world()
        .component( PublicAccessApplication.MyComponent.class )
        .stage( "sim" )
        .subSystem( new PublicAccessApplication_Galdr_MySubSystem() )
        .endStage()
        .build();
    _sim = world.getStageByName( "sim" );
  }

  @Override
  @Nonnull
  public Stage sim() {
    return _sim;
  }
}
