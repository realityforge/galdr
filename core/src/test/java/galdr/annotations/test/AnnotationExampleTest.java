package galdr.annotations.test;

import galdr.AbstractTest;
import galdr.ComponentAPI;
import galdr.ComponentStorage;
import galdr.ProcessorStage;
import galdr.World;
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
    final MyApplication application = MyApplication.create();
    final World world = application.world();

    final ProcessorStage stage = application.sim();

    stage.process( 1 );

    final int entityId = createEntity( world );

    stage.process( 1 );

    final ComponentAPI<Health> api = application.health();

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
