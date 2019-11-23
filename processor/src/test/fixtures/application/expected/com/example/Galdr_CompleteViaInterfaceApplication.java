package com.example;

import com.example.other.CompleteInterfaceApplication;
import com.example.other.CompleteInterfaceApplication_Galdr_MySubSystem1;
import com.example.other.CompleteInterfaceApplication_Galdr_MySubSystem2;
import com.example.other.CompleteInterfaceApplication_Galdr_MySubSystem3;
import galdr.Stage;
import galdr.World;
import galdr.Worlds;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("galdr.processor.ApplicationProcessor")
final class Galdr_CompleteViaInterfaceApplication extends CompleteViaInterfaceApplication {
  @Nonnull
  private final Stage _sim;

  @Nonnull
  private final Stage _render;

  Galdr_CompleteViaInterfaceApplication() {
    final World world = Worlds
        .world()
        .component( CompleteInterfaceApplication.MyComponent.class )
        .stage( "sim" )
        .subSystem( new CompleteInterfaceApplication_Galdr_MySubSystem1() )
        .subSystem( new CompleteInterfaceApplication_Galdr_MySubSystem2() )
        .endStage()
        .stage( "render" )
        .subSystem( new CompleteInterfaceApplication_Galdr_MySubSystem3() )
        .endStage()
        .build();
    _sim = world.getStageByName( "sim" );
    _render = world.getStageByName( "render" );
  }

  @Override
  @Nonnull
  public Stage sim() {
    return _sim;
  }

  @Override
  @Nonnull
  public Stage render() {
    return _render;
  }
}
