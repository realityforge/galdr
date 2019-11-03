package galdr.annotations.test;

import galdr.ProcessorStage;
import galdr.World;
import galdr.Worlds;
import javax.annotation.Nonnull;

final class Galdr_MyApplication
  extends MyApplication
{
  @Nonnull
  private final ProcessorStage _sim;

  Galdr_MyApplication()
  {
    final World world = Worlds
      .world()
      .component( Health.class, Health::new )
      .component( MyFlag.class )
      .stage( "sim" )
      .processor( new Galdr_HealthProcessor() )
      .endStage()
      .build();
    _sim = world.getStageByName( "sim" );
  }

  @Nonnull
  @Override
  ProcessorStage sim()
  {
    return _sim;
  }
}
