package galdr.annotations.test;

import galdr.AbstractTest;
import galdr.ComponentAPI;
import galdr.ComponentStorage;
import galdr.ProcessorStage;
import galdr.World;
import galdr.Worlds;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class AnnotationExampleTest
  extends AbstractTest
{
  @Test
  public void basicOperation()
  {
    // This is a very basic test that is used to try out the developer experience of
    // using the annotation development driven model
    final World world =
      Worlds
        .world()
        .component( Health.class, Health::new )
        .component( MyFlag.class )
        .stage( "sim", new Galdr_HealthProcessor() )
        .build();

    final ProcessorStage stage = world.getStageByName( "sim" );

    stage.process( 1 );

    final int entityId = createEntity( world );

    stage.process( 1 );

    final ComponentAPI<Health> api = world.getComponentByType( Health.class );

    assertEquals( api.getId(), 0 );
    assertEquals( api.getStorage(), ComponentStorage.ARRAY );

    run( world, () -> assertFalse( api.has( entityId ) ) );
    run( world, () -> assertNull( api.find( entityId ) ) );

    final Health health = run( world, () -> api.create( entityId ) );
    health.healthPoints = 23;

    stage.process( 1 );

    stage.process( 1 );
  }
}
