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
  public void component()
  {
    final String name = randomString();
    final World world =
      Galdr.world( name )
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
      Galdr.world( name )
        .stage( "ABC" )
        .stage( "DEF" )
        .build();

    final Map<String, ProcessorStage> stages = world.getStages();
    assertEquals( stages.size(), 2 );
    assertTrue( stages.containsKey( "ABC" ) );
    assertTrue( stages.containsKey( "DEF" ) );
  }

  @Test
  public void build_invokedAfterWorldBuild()
  {
    final WorldBuilder builder = Galdr.world();
    builder.build();

    assertInvariantFailure( builder::build,
                            "Galdr-0019: Attempted to invoke method on WorldBuilder but world has already been constructed" );
  }

  @Test
  public void component_invokedAfterWorldBuild()
  {
    final WorldBuilder builder = Galdr.world();
    builder.build();

    assertInvariantFailure( () -> builder.stage( randomString() ),
                            "Galdr-0019: Attempted to invoke method on WorldBuilder but world has already been constructed" );
  }

  @Test
  public void stage_invokedAfterWorldBuild()
  {
    final WorldBuilder builder = Galdr.world();
    builder.build();

    assertInvariantFailure( () -> builder.component( Attack.class, Attack::new ),
                            "Galdr-0019: Attempted to invoke method on WorldBuilder but world has already been constructed" );
  }
}
