package com.example.stage;

import galdr.Stage;
import galdr.World;
import galdr.Worlds;
import javax.annotation.Nonnull;
import javax.annotation.processing.Generated;

@Generated("galdr.processor.ApplicationProcessor")
final class Galdr_NonStandardMethodNameStage extends NonStandardMethodNameStage {
  @Nonnull
  private final Stage _$4v3;

  Galdr_NonStandardMethodNameStage() {
    final World world = Worlds
        .world()
        .component( NonStandardMethodNameStage.MyComponent.class )
        .stage( "$4v3" )
        .subSystem( new NonStandardMethodNameStage_Galdr_MySubSystem() )
        .endStage()
        .build();
    _$4v3 = world.getStageByName( "$4v3" );
  }

  @Override
  @Nonnull
  Stage $4v3() {
    return _$4v3;
  }
}
