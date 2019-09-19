package galdr;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class WorldTest
  extends AbstractTest
{
  private static class Component1
  {
  }

  private static class Component2
  {
  }

  @Test
  public void basicConstruct()
  {
    final World world1 = Galdr.world( "Foo" ).build();
    final World world2 = Galdr.world().build();

    assertEquals( world1.getName(), "Foo" );
    assertEquals( world1.toString(), "World[Foo]" );
    assertEquals( world2.getName(), "World@1" );
    assertEquals( world2.toString(), "World[World@1]" );
  }

  @Test
  public void getStages()
  {
    final String name1 = "ABCDEF";
    final String name2 = "GHIJK";
    final String name3 = randomString();
    final World world = Galdr.world()
      .stage( name1, new BasicNoopProcessor() )
      .stage( name2, new BasicNoopProcessor() )
      .build();

    final Map<String, ProcessorStage> stages = world.getStages();
    assertEquals( stages.size(), 2 );
    assertTrue( stages.containsKey( name1 ) );
    assertTrue( stages.containsKey( name2 ) );
    assertFalse( stages.containsKey( name3 ) );
  }

  @Test
  public void getStages_PreConstruct()
  {
    final String name = randomString();
    final World world = new World( name );
    assertInvariantFailure( world::getStages,
                            "Galdr-0045: Attempted to invoke World.getStages() on World named '" + name +
                            "' prior to World completing construction" );
  }

  @Test
  public void getStageByName()
  {
    final String name1 = "ABCDEF";
    final String name2 = "GHIJK";
    final String name3 = randomString();
    final World world = Galdr.world()
      .stage( name1, new BasicNoopProcessor() )
      .stage( name2, new BasicNoopProcessor() )
      .build();

    assertNotNull( world.getStageByName( name1 ) );
    assertNotNull( world.getStageByName( name2 ) );
    assertInvariantFailure( () -> world.getStageByName( name3 ),
                            "Galdr-0046: Invoked World.getStageByName() on World named 'World@1' with " +
                            "stage name '" + name3 + "' but no such stage exists. Known stages include: [" +
                            "ABCDEF, GHIJK]" );
  }

  @Test
  public void getComponentByType()
  {
    final World world = Galdr.world().component( Component1.class, Component1::new ).build();

    assertNotNull( world.getComponentByType( Component1.class ) );
  }

  @Test
  public void getComponentRegistry()
  {
    final World world = Galdr.world()
      .component( Component1.class, Component1::new )
      .component( Component2.class, Component2::new )
      .build();

    final ComponentRegistry componentRegistry = world.getComponentRegistry();
    final Set<Class<?>> componentTypes = componentRegistry.getComponentTypes();
    assertEquals( componentTypes.size(), 2 );
    assertTrue( componentTypes.contains( Component1.class ) );
    assertTrue( componentTypes.contains( Component2.class ) );
  }

  @Test
  public void getComponentRegistry_PreConstruct()
  {
    final String name = randomString();
    final World world = new World( name );
    assertInvariantFailure( world::getComponentRegistry,
                            "Galdr-0044: Attempted to invoke World.getComponentRegistry() on World named '" +
                            name + "' prior to World completing construction" );
  }

  @Test
  public void errorHandlerLifecycle()
  {
    final String name = randomString();
    final WorldBuilder builder = Galdr.world();
    final Processor processor = new BasicNoopProcessor();
    builder.stage( name, processor );
    final World world = builder.build();

    final ProcessorStage stage = world.getStageByName( name );
    final Throwable throwable = new Throwable();

    final AtomicInteger callCount = new AtomicInteger();

    final ErrorHandler handler = ( s, p, t ) -> {
      callCount.incrementAndGet();
      assertEquals( s, stage );
      assertEquals( p, processor );
      assertEquals( t, throwable );
    };

    world.addErrorHandler( handler );

    assertEquals( world.getErrorHandlerSupport().getHandlers().size(), 1 );
    assertTrue( world.getErrorHandlerSupport().getHandlers().contains( handler ) );

    assertEquals( callCount.get(), 0 );

    world.reportError( stage, processor, throwable );

    assertEquals( callCount.get(), 1 );

    world.removeErrorHandler( handler );

    assertEquals( world.getErrorHandlerSupport().getHandlers().size(), 0 );

    world.reportError( stage, processor, throwable );

    assertEquals( callCount.get(), 1 );
  }

  @Test
  public void addErrorHandler_whenDisabled()
  {
    GaldrTestUtil.disableErrorHandlers();

    final World world = Galdr.world().build();

    final ErrorHandler errorHandler = ( stage, processor, throwable ) -> {
    };

    assertInvariantFailure( () -> world.addErrorHandler( errorHandler ),
                            "Galdr-0182: World.addErrorHandler() invoked when Galdr.areErrorHandlersEnabled() returns false." );
  }

  @Test
  public void removeErrorHandler_whenDisabled()
  {
    GaldrTestUtil.disableErrorHandlers();

    final World world = Galdr.world().build();

    final ErrorHandler errorHandler = ( stage, processor, throwable ) -> {
    };
    assertInvariantFailure( () -> world.removeErrorHandler( errorHandler ),
                            "Galdr-0181: World.removeErrorHandler() invoked when Galdr.areErrorHandlersEnabled() returns false." );
  }
}
