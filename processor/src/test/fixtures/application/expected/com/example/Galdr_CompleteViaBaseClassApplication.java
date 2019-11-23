package com.example;

import com.example.other.CompleteBaseApplication;
import com.example.other.CompleteBaseApplication_Galdr_MySubSystem1;
import com.example.other.CompleteBaseApplication_Galdr_MySubSystem2;
import com.example.other.CompleteBaseApplication_Galdr_MySubSystem3;
import galdr.Stage;
import galdr.World;
import galdr.Worlds;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("galdr.processor.ApplicationProcessor")
final class Galdr_CompleteViaBaseClassApplication extends CompleteViaBaseClassApplication {
  @Nonnull
  private final Stage _sim;

  @Nonnull
  private final Stage _render;

  Galdr_CompleteViaBaseClassApplication() {
    final World world = Worlds
        .world()
        .component( CompleteBaseApplication.MyComponent.class )
        .stage( "sim" )
        .subSystem( new CompleteBaseApplication_Galdr_MySubSystem1() )
        .subSystem( new CompleteBaseApplication_Galdr_MySubSystem2() )
        .endStage()
        .stage( "render" )
        .subSystem( new CompleteBaseApplication_Galdr_MySubSystem3() )
        .endStage()
        .build();
    _sim = world.getStageByName( "sim" );
    _render = world.getStageByName( "render" );
  }

  @Override
  @Nonnull
  protected Stage sim() {
    return _sim;
  }

  @Override
  @Nonnull
  protected Stage render() {
    return _render;
  }
}
