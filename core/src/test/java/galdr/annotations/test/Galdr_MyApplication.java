package galdr.annotations.test;

import galdr.ComponentManager;
import galdr.ProcessorStage;
import galdr.World;
import galdr.Worlds;
import javax.annotation.Nonnull;

final class Galdr_MyApplication
  extends MyApplication
{
  @Nonnull
  private final World _world;
  @Nonnull
  private final ProcessorStage _sim;
  @Nonnull
  private final ComponentManager<Health> _health;

  Galdr_MyApplication()
  {
    _world = Worlds
      .world()
      .component( Health.class, Health::new )
      .component( MyFlag.class )
      .stage( "sim" )
      .processor( new Galdr_HealthProcessor() )
      .endStage()
      .build();
    _sim = _world.getStageByName( "sim" );
    _health = _world.getComponentByType( Health.class );
  }

  @Nonnull
  @Override
  public World world()
  {
    return _world;
  }

  @Nonnull
  @Override
  ProcessorStage sim()
  {
    return _sim;
  }

  @Nonnull
  @Override
  ComponentManager<Health> health()
  {
    return _health;
  }
}
