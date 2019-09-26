package galdr;

import java.util.Map;
import java.util.Set;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class WorldBuilderTest
  extends AbstractTest
{
  private static class Armour
  {
  }

  private static class Health
  {
  }

  private static class Attack
  {
  }

  @Test
  public void initialEntityCount()
  {
    final String name = randomString();
    final World world =
      Worlds.world( name )
        .initialEntityCount( 233 )
        .build();

    assertEquals( world.getEntityManager().capacity(), 233 );
  }

  @Test
  public void initialEntityCount_default()
  {
    final String name = randomString();
    final World world = Worlds.world( name ).build();
    assertEquals( world.getEntityManager().capacity(), WorldBuilder.DEFAULT_INITIAL_ENTITY_COUNT );
  }

  @Test
  public void initialEntityCount_invalid()
  {
    final WorldBuilder builder = Worlds.world();

    assertInvariantFailure( () -> builder.initialEntityCount( -2 ),
                            "Galdr-007: Attempted to set initialEntityCount to -2 for world named 'World@1' but initialEntityCount must be a positive value." );
  }

  @Test
  public void component()
  {
    final String name = randomString();
    final World world =
      Worlds.world( name )
        .component( Armour.class, Armour::new )
        .component( Health.class, Health::new )
        .build();

    final Set<Class<?>> componentTypes = world.getComponentRegistry().getComponentTypes();
    assertEquals( componentTypes.size(), 2 );
    assertTrue( componentTypes.contains( Armour.class ) );
    assertTrue( componentTypes.contains( Health.class ) );
  }

  @Test
  public void stage()
  {
    final String name = randomString();
    final World world =
      Worlds.world( name )
        .stage( "ABC" )
        .stage( "DEF" )
        .build();

    final Map<String, ProcessorStage> stages = world.getStages();
    assertEquals( stages.size(), 2 );
    assertTrue( stages.containsKey( "ABC" ) );
    assertTrue( stages.containsKey( "DEF" ) );
  }

  @Test
  public void stage_with_DuplicateName()
  {
    final String name = randomString();
    final WorldBuilder builder = Worlds.world( name );
    builder.stage( "DEF" );
    builder.stage( "ABC" );

    assertInvariantFailure( () -> builder.stage( "DEF" ),
                            "Galdr-0087: Attempted to create stage named named 'DEF' but a stage already exists with the specified name. Existing stages include: [ABC, DEF]" );
  }

  @Test
  public void build_invokedAfterWorldBuild()
  {
    final WorldBuilder builder = Worlds.world();
    builder.build();

    assertInvariantFailure( builder::build,
                            "Galdr-0019: Attempted to invoke method on WorldBuilder but world has already been constructed" );
  }

  @Test
  public void initialEntityCount_invokedAfterWorldBuild()
  {
    final WorldBuilder builder = Worlds.world();
    builder.build();

    assertInvariantFailure( () -> builder.initialEntityCount( 22 ),
                            "Galdr-0019: Attempted to invoke method on WorldBuilder but world has already been constructed" );
  }

  @Test
  public void component_invokedAfterWorldBuild()
  {
    final WorldBuilder builder = Worlds.world();
    builder.build();

    assertInvariantFailure( () -> builder.stage( randomString() ),
                            "Galdr-0019: Attempted to invoke method on WorldBuilder but world has already been constructed" );
  }

  @Test
  public void stage_invokedAfterWorldBuild()
  {
    final WorldBuilder builder = Worlds.world();
    builder.build();

    assertInvariantFailure( () -> builder.component( Attack.class, Attack::new ),
                            "Galdr-0019: Attempted to invoke method on WorldBuilder but world has already been constructed" );
  }
}
