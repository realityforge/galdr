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
    final Galdr_HealthProcessor processor = (Galdr_HealthProcessor) new Galdr_HealthProcessorFactory().get();
    final World world =
      Worlds
        .world()
        .component( Health.class, Health::new )
        .component( MyFlag.class )
        .stage( "sim", processor )
        .build();

    run( world, processor::postConstruct );

    final ProcessorStage stage = world.getStageByName( "sim" );

    stage.process( 1 );

    final int entityId = createEntity( world, set() );

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
