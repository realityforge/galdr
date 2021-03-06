package galdr.integration.basic;

import galdr.ComponentManager;
import galdr.World;
import galdr.annotations.ComponentManagerRef;
import galdr.annotations.EntityProcessor;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.WorldRef;
import javax.annotation.Nonnull;

@GaldrSubSystem
abstract class HealthProcessor
{
  @ComponentManagerRef
  @Nonnull
  abstract ComponentManager<Health> health();

  @WorldRef
  @Nonnull
  abstract World world();

  @EntityProcessor( all = Health.class )
  final void processHealth( final int delta, final int entityId )
  {
    System.out.println( "processHealth(" +
                        "delta=" + delta +
                        ",entityId=" + entityId +
                        ",healthPoints=" + health().get( entityId ).healthPoints +
                        ")" );
  }
}
