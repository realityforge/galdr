package galdr.annotations.test;

import galdr.ComponentAPI;
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
  private final ComponentAPI<Health> _health;

  Galdr_MyApplication()
  {
    _world = Worlds
      .world()
      .component( Health.class, Health::new )
      .component( MyFlag.class )
      .stage( "sim", new Galdr_HealthProcessor() )
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
  ComponentAPI<Health> health()
  {
    return _health;
  }
}
