package galdr.annotations.test;

import galdr.ComponentManager;
import galdr.ComponentStorage;
import galdr.World;
import galdr.annotations.ComponentManagerRef;
import galdr.annotations.EntityProcessor;
import galdr.annotations.Processor;
import galdr.annotations.SubSystem;
import galdr.annotations.WorldRef;
import javax.annotation.Nonnull;
import static org.testng.Assert.*;

@SubSystem
abstract class HealthProcessor
{
  private int _frame;

  @ComponentManagerRef
  @Nonnull
  abstract ComponentManager<Health> health();

  @WorldRef
  @Nonnull
  abstract World world();

  @Processor
  final void processGlobalActions()
  {
    _frame++;

    final ComponentManager<Health> api = health();

    if ( 1 == _frame )
    {
      assertEquals( api.getId(), 0 );
      assertEquals( api.getStorage(), ComponentStorage.ARRAY );
    }
    else if ( 2 == _frame )
    {
      final int entityId = world().createEntity( Health.class );
      assertTrue( api.has( entityId ) );
      final Health health = api.find( entityId );
      assertNotNull( health );
      health.healthPoints = 23;
    }
  }

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
