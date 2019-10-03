package galdr;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
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

  private static class Component3
  {
  }

  @Test
  public void basicConstruct()
  {
    final World world1 = Worlds.world( "Foo" ).build();
    final World world2 = Worlds.world().build();

    assertEquals( world1.getName(), "Foo" );
    assertEquals( world1.toString(), "World[Foo]" );
    assertEquals( world2.getName(), "World@1" );
    assertEquals( world2.toString(), "World[World@1]" );
  }

  @Test
  public void toString_test()
  {
    final World world1 = Worlds.world( "Foo" ).build();
    final World world2 = Worlds.world().build();

    assertEquals( world1.toString(), "World[Foo]" );
    assertEquals( world2.toString(), "World[World@1]" );

    GaldrTestUtil.disableDebugToString();

    assertDefaultToString( world1 );
    assertDefaultToString( world2 );
  }

  @Test
  public void getName()
  {
    assertEquals( Worlds.world( "Foo" ).build().getName(), "Foo" );
    assertEquals( Worlds.world().build().getName(), "World@1" );

    GaldrTestUtil.disableNames();

    final World world = Worlds.world().build();
    assertInvariantFailure( world::getName,
                            "Galdr-0004: World.getName() invoked when Galdr.areNamesEnabled() returns false" );
  }

  @Test
  public void getStages()
  {
    final String name1 = "ABCDEF";
    final String name2 = "GHIJK";
    final String name3 = randomString();
    final World world = Worlds.world()
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
    final World world = Worlds.world()
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
    final World world = Worlds.world().component( Component1.class, Component1::new ).build();

    assertNotNull( world.getComponentByType( Component1.class ) );
  }

  @Test
  public void getComponentTypes()
  {
    final World world = Worlds.world()
      .component( Component1.class, Component1::new )
      .component( Component2.class, Component2::new )
      .component( Component3.class, Component3::new )
      .build();

    assertEquals( world.getComponentCount(), 3 );
    assertTypeRegistered( world, Component1.class, 0 );
    assertTypeRegistered( world, Component2.class, 1 );
    assertTypeRegistered( world, Component3.class, 2 );

    assertEquals( world.getComponentTypes(),
                  new HashSet<>( Arrays.asList( Component1.class, Component2.class, Component3.class ) ) );
  }

  @Test
  public void basicEntityAPIInteractions()
  {
    final World world = Worlds.world().build();

    final int entityId1 = createEntity( world, set() );
    final int entityId2 = createEntity( world, set() );

    assertTrue( world.isEntity( entityId1 ) );
    assertTrue( world.isEntity( entityId2 ) );

    run( world, () -> world.disposeEntity( entityId1 ) );

    assertFalse( world.isEntity( entityId1 ) );
    assertTrue( world.isEntity( entityId2 ) );

    final int entityId3 = createEntity( world, set() );

    assertTrue( world.isEntity( entityId2 ) );
    assertTrue( world.isEntity( entityId3 ) );
  }

  @Test
  public void getEntityManager_PreConstruct()
  {
    final String name = randomString();
    final World world = new World( name );
    assertInvariantFailure( world::getEntityManager,
                            "Galdr-0045: Attempted to invoke World.getEntityManager() on World named '" +
                            name + "' prior to World completing construction" );
  }

  @Test
  public void errorHandlerLifecycle()
  {
    final String name = randomString();
    final Worlds.Builder builder = Worlds.world();
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

    final World world = Worlds.world().build();

    final ErrorHandler errorHandler = ( stage, processor, throwable ) -> {
    };

    assertInvariantFailure( () -> world.addErrorHandler( errorHandler ),
                            "Galdr-0182: World.addErrorHandler() invoked when Galdr.areErrorHandlersEnabled() returns false." );
  }

  @Test
  public void removeErrorHandler_whenDisabled()
  {
    GaldrTestUtil.disableErrorHandlers();

    final World world = Worlds.world().build();

    final ErrorHandler errorHandler = ( stage, processor, throwable ) -> {
    };
    assertInvariantFailure( () -> world.removeErrorHandler( errorHandler ),
                            "Galdr-0181: World.removeErrorHandler() invoked when Galdr.areErrorHandlersEnabled() returns false." );
  }

  @Test
  public void getSpy_whenSpiesDisabled()
  {
    GaldrTestUtil.disableSpies();

    final World world = Worlds.world().build();

    assertInvariantFailure( world::getSpy, "Galdr-0021: Attempting to get Spy but spies are not enabled." );
  }

  @Test
  public void run_action()
  {
    final World world = Worlds.world().build();

    assertInvariantFailure( WorldHolder::world, "Galdr-0026: Invoked WorldHolder.world() when no world was active." );
    world.run( () -> assertEquals( WorldHolder.world(), world ) );
    assertInvariantFailure( WorldHolder::world, "Galdr-0026: Invoked WorldHolder.world() when no world was active." );
  }

  @Test
  public void run_function()
  {
    final World world = Worlds.world().build();

    assertInvariantFailure( WorldHolder::world, "Galdr-0026: Invoked WorldHolder.world() when no world was active." );
    final int value = world.run( () -> {
      assertEquals( WorldHolder.world(), world );
      return 111;
    } );
    assertInvariantFailure( WorldHolder::world, "Galdr-0026: Invoked WorldHolder.world() when no world was active." );
    assertEquals( value, 111 );
  }

  @Test
  public void constructContainingComponentsWithInvalidIndex()
  {
    Worlds.world();
    final World world = WorldHolder.world();
    final ComponentManager<?>[] components = new ComponentManager[]
      {
        new FastArrayComponentManager<>( world, 0, Component1.class, Component1::new, 120 ),
        new FastArrayComponentManager<>( world, 1, Component2.class, Component2::new, 120 ),
        new FastArrayComponentManager<>( world, 3, Component3.class, Component3::new, 120 )
      };
    assertInvariantFailure( () -> world.completeConstruction( 100, components, Collections.emptyMap() ),
                            "Galdr-0003: Component named 'Component3' has id 3 but was passed as index 2." );
  }

  @Test
  public void isComponentIdValid()
  {
    final World world = Worlds.world()
      .component( Component1.class, Component1::new )
      .build();
    assertFalse( world.isComponentIdValid( -1 ) );
    assertTrue( world.isComponentIdValid( 0 ) );
    assertFalse( world.isComponentIdValid( 1 ) );
  }

  @Test
  public void getComponentManagerByIndex_badIndex()
  {
    final World world = Worlds.world()
      .component( Component1.class, Component1::new )
      .build();
    assertInvariantFailure( () -> world.getComponentManagerById( -1 ),
                            "Galdr-0002: World.getComponentManagerByIndex() attempted to access Component with id -1 but no such component exists." );
    assertInvariantFailure( () -> world.getComponentManagerById( 1 ),
                            "Galdr-0002: World.getComponentManagerByIndex() attempted to access Component with id 1 but no such component exists." );
  }

  @Test
  public void getComponentManagerByType_badType()
  {
    final World world = Worlds.world().build();
    assertInvariantFailure( () -> world.getComponentManagerByType( Component2.class ),
                            "Galdr-0001: World.getComponentManagerByType() attempted to access Component for type class galdr.WorldTest$Component2 but no such component exists." );
  }

  private void assertTypeRegistered( @Nonnull final World world,
                                     @Nonnull final Class<?> type,
                                     final int id )
  {
    final ComponentManager<?> entry = world.getComponentManagerById( id );
    assertEquals( entry.getId(), id );
    assertEquals( entry.getType(), type );
    assertEquals( world.getComponentManagerByType( type ), entry );
  }
}
