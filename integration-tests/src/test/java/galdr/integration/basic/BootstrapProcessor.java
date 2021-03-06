package galdr.integration.basic;

import galdr.ComponentManager;
import galdr.ComponentStorage;
import galdr.World;
import galdr.annotations.ComponentManagerRef;
import galdr.annotations.GaldrSubSystem;
import galdr.annotations.NameRef;
import galdr.annotations.Processor;
import galdr.annotations.WorldRef;
import javax.annotation.Nonnull;
import static org.testng.Assert.*;

@GaldrSubSystem
abstract class BootstrapProcessor
{
  private int _frame;

  @ComponentManagerRef
  @Nonnull
  abstract ComponentManager<Health> health();

  @WorldRef
  @Nonnull
  abstract World world();

  @NameRef
  @Nonnull
  abstract String getName();

  @Processor
  final void processGlobalActions()
  {
    System.out.println( getName() + ".process()" );
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
}
