package com.example.component;

import galdr.Stage;
import galdr.World;
import galdr.Worlds;
import javax.annotation.Nonnull;
import javax.annotation.processing.Generated;

@Generated("galdr.processor.ApplicationProcessor")
final class Galdr_MultipleComponentApplication extends MultipleComponentApplication {
  @Nonnull
  private final Stage _sim;

  Galdr_MultipleComponentApplication() {
    final World world = Worlds
        .world()
        .component( MultipleComponentApplication.MyComponent1.class )
        .component( MultipleComponentApplication.MyComponent2.class )
        .component( MultipleComponentApplication.MyComponent3.class )
        .stage( "sim" )
        .subSystem( new MultipleComponentApplication_Galdr_MySubSystem() )
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
