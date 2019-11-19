package galdr.integration.basic;

import galdr.Stage;
import galdr.World;
import galdr.Worlds;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated( "galdr.processor.SubSystemProcessor" )
final class Galdr_MyApplication
  extends MyApplication
{
  @Nonnull
  private final Stage _sim;

  Galdr_MyApplication()
  {
    final World world = Worlds
      .world()
      .component( Health.class, Health::new )
      .component( MyFlag.class )
      .stage( "sim" )
      .subSystem( new Galdr_BootstrapProcessor() )
      .subSystem( new Galdr_HealthProcessor() )
      .endStage()
      .build();
    _sim = world.getStageByName( "sim" );
  }

  @Nonnull
  @Override
  Stage sim()
  {
    return _sim;
  }
}
