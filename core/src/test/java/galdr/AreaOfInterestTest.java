package galdr;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class AreaOfInterestTest
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

  private static class Component4
  {
  }

  private static class Component5
  {
  }

  private static class Component6
  {
  }

  private static class Component7
  {
  }

  @Test
  public void construct()
  {
    final World world = Worlds.world().component( Component1.class ).build();

    final AreaOfInterest areaOfInterest = world.createAreaOfInterest( Collections.singletonList( Component1.class ) );

    assertEquals( areaOfInterest.getAll().getBitSet(), set( 0 ) );
    assertEquals( areaOfInterest.getOne().getBitSet(), set() );
    assertEquals( areaOfInterest.getExclude().getBitSet(), set() );
  }

  @Test
  public void construct_overlappingBitsets()
  {
    final World world = Worlds.world()
      .component( Component1.class )
      .component( Component2.class )
      .component( Component3.class )
      .component( Component4.class )
      .component( Component5.class )
      .build();

    final Collection<Class<?>> set0 = Collections.emptySet();
    final Collection<Class<?>> set1 = Arrays.asList( Component1.class, Component2.class, Component3.class );
    final Collection<Class<?>> set2 = Arrays.asList( Component3.class, Component4.class, Component5.class );

    assertInvariantFailure( () -> world.createAreaOfInterest( set1, set2, set0 ),
                            "Galdr-0005: AreaOfInterest passed intersecting BitSets all ({0, 1, 2}) and one ({2, 3, 4})." );
    assertInvariantFailure( () -> world.createAreaOfInterest( set1, set0, set2 ),
                            "Galdr-0005: AreaOfInterest passed intersecting BitSets all ({0, 1, 2}) and exclude ({2, 3, 4})." );
    assertInvariantFailure( () -> world.createAreaOfInterest( set0, set1, set2 ),
                            "Galdr-0005: AreaOfInterest passed intersecting BitSets one ({0, 1, 2}) and exclude ({2, 3, 4})." );
  }

  @Test
  public void construct_singleOneRequirement()
  {
    final World world = Worlds.world()
      .component( Component1.class )
      .component( Component2.class )
      .build();

    final Collection<Class<?>> set1 = Collections.singletonList( Component1.class );
    final Collection<Class<?>> set2 = Collections.singletonList( Component2.class );

    assertInvariantFailure( () -> world.createAreaOfInterest( set1, set2 ),
                            "Galdr-0051: World.createAreaOfInterest() passed a single component in the one component set. This AreaOfInterest must have multiple components in the one component set of the component should be moved to the all component set." );
  }

  @Test
  public void construct_zeroRequirements()
  {
    final World world = Worlds.world().build();

    assertInvariantFailure( () -> world.createAreaOfInterest( Collections.emptySet() ),
                            "Galdr-0053: World.createAreaOfInterest() attempted to create an AreaOfInterest with no requirements." );
  }

  @Test
  public void matches()
  {
    final World world = Worlds.world()
      .component( Component1.class )
      .component( Component2.class )
      .component( Component3.class )
      .component( Component4.class )
      .component( Component5.class )
      .component( Component6.class )
      .component( Component7.class )
      .build();

    // Matches - huzzah
    assertMatch( world,
                 Arrays.asList( Component1.class, Component2.class ),
                 Arrays.asList( Component3.class, Component4.class ),
                 Arrays.asList( Component5.class, Component6.class ),
                 Arrays.asList( Component1.class, Component2.class, Component3.class, Component7.class ) );

    // Matches and includes both ones
    assertMatch( world,
                 Arrays.asList( Component1.class, Component2.class ),
                 Arrays.asList( Component3.class, Component4.class ),
                 Arrays.asList( Component5.class, Component6.class ),
                 Arrays.asList( Component1.class,
                                Component2.class,
                                Component3.class,
                                Component4.class,
                                Component7.class ) );

    // Missing all
    assertNoMatch( world,
                   Arrays.asList( Component1.class, Component2.class ),
                   Arrays.asList( Component3.class, Component4.class ),
                   Arrays.asList( Component5.class, Component6.class ),
                   Arrays.asList( Component2.class, Component3.class, Component7.class ) );

    // Missing all one's
    assertNoMatch( world,
                   Arrays.asList( Component1.class, Component2.class ),
                   Arrays.asList( Component3.class, Component4.class ),
                   Arrays.asList( Component5.class, Component6.class ),
                   Arrays.asList( Component1.class, Component2.class, Component7.class ) );

    // Includes exclude
    assertNoMatch( world,
                   Arrays.asList( Component1.class, Component2.class ),
                   Arrays.asList( Component3.class, Component4.class ),
                   Arrays.asList( Component5.class, Component6.class ),
                   Arrays.asList( Component1.class,
                                  Component2.class,
                                  Component3.class,
                                  Component5.class,
                                  Component7.class ) );

    // Includes multiple excludes
    assertNoMatch( world,
                   Arrays.asList( Component1.class, Component2.class ),
                   Arrays.asList( Component3.class, Component4.class ),
                   Arrays.asList( Component5.class, Component6.class ),
                   Arrays.asList( Component1.class,
                                  Component2.class,
                                  Component3.class,
                                  Component5.class,
                                  Component6.class,
                                  Component7.class ) );
  }

  private void assertMatch( @Nonnull final World world,
                            @Nonnull final Collection<Class<?>> all,
                            @Nonnull final Collection<Class<?>> one,
                            @Nonnull final Collection<Class<?>> exclude,
                            @Nonnull final Collection<Class<?>> componentIds )
  {
    _assertMatch( world, all, one, exclude, componentIds, true );
  }

  private void assertNoMatch( @Nonnull final World world,
                              @Nonnull final Collection<Class<?>> all,
                              @Nonnull final Collection<Class<?>> one,
                              @Nonnull final Collection<Class<?>> exclude,
                              @Nonnull final Collection<Class<?>> componentIds )
  {
    _assertMatch( world, all, one, exclude, componentIds, false );
  }

  private void _assertMatch( @Nonnull final World world,
                             @Nonnull final Collection<Class<?>> all,
                             @Nonnull final Collection<Class<?>> one,
                             @Nonnull final Collection<Class<?>> exclude,
                             @Nonnull final Collection<Class<?>> componentIds,
                             final boolean shouldMatch )
  {
    final AreaOfInterest areaOfInterest = world.createAreaOfInterest( all, one, exclude );
    final ComponentIdSet componentIdSet = world.createComponentIdSet( componentIds );
    assertEquals( areaOfInterest.matches( componentIdSet.getBitSet() ), shouldMatch );
  }

  @Test
  public void toString_test()
  {
    final World world = Worlds.world()
      .component( Component1.class )
      .component( Component2.class )
      .component( Component3.class )
      .component( Component4.class )
      .build();

    final Collection<Class<?>> all = Collections.singletonList( Component3.class );
    final Collection<Class<?>> one = Arrays.asList( Component2.class, Component1.class );
    final Collection<Class<?>> exclude = Collections.emptyList();

    final AreaOfInterest areaOfInterest = world.createAreaOfInterest( all, one, exclude );

    assertEquals( areaOfInterest.toString(), "AreaOfInterest[all={2},one={0, 1},exclude={}]" );

    GaldrTestUtil.disableDebugToString();

    assertDefaultToString( areaOfInterest );
  }

  @Test
  public void hash_and_equals()
  {
    final World world = Worlds.world()
      .component( Component1.class )
      .component( Component2.class )
      .component( Component3.class )
      .component( Component4.class )
      .component( Component5.class )
      .build();

    final Collection<Class<?>> set1 = Collections.singletonList( Component1.class );
    final Collection<Class<?>> set2 = Arrays.asList( Component2.class, Component5.class );
    final Collection<Class<?>> set3 = Collections.singletonList( Component3.class );
    final Collection<Class<?>> set4 = Collections.singletonList( Component4.class );

    final AreaOfInterest aoi1 = world.createAreaOfInterest( set1, set2, set3 );
    final AreaOfInterest aoi2 = world.createAreaOfInterest( set1, set2, set3 );
    final AreaOfInterest aoi3 = world.createAreaOfInterest( set1, set2, set4 );

    assertEquals( aoi1.hashCode(), aoi1.hashCode() );
    assertEquals( aoi1.hashCode(), aoi2.hashCode() );
    assertNotEquals( aoi1.hashCode(), aoi3.hashCode() );

    assertEquals( aoi1, aoi1 );
    assertEquals( aoi1, aoi2 );
    assertNotEquals( aoi1, aoi3 );
    //noinspection EqualsBetweenInconvertibleTypes,SimplifiedTestNGAssertion
    assertFalse( aoi1.equals( "" ) );
  }
}
