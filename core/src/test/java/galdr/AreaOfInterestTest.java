package galdr;

import java.util.BitSet;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class AreaOfInterestTest
  extends AbstractTest
{
  @Test
  public void construct()
  {
    final BitSet all = set();
    final BitSet one = set();
    final BitSet exclude = set();

    final AreaOfInterest areaOfInterest = new AreaOfInterest( all, one, exclude );

    assertEquals( areaOfInterest.getAll(), all );
    assertEquals( areaOfInterest.getOne(), one );
    assertEquals( areaOfInterest.getExclude(), exclude );
  }

  @Test
  public void construct_overlappingBitsets()
  {
    final BitSet set0 = set();
    final BitSet set1 = set( 0, 1, 2 );
    final BitSet set2 = set( 2, 3, 4 );

    assertInvariantFailure( () -> new AreaOfInterest( set1, set2, set0 ),
                            "Galdr-0005: AreaOfInterest passed intersecting BitSets all ({0, 1, 2}) and one ({2, 3, 4})." );
    assertInvariantFailure( () -> new AreaOfInterest( set1, set0, set2 ),
                            "Galdr-0005: AreaOfInterest passed intersecting BitSets all ({0, 1, 2}) and exclude ({2, 3, 4})." );
    assertInvariantFailure( () -> new AreaOfInterest( set0, set1, set2 ),
                            "Galdr-0005: AreaOfInterest passed intersecting BitSets one ({0, 1, 2}) and exclude ({2, 3, 4})." );
  }

  @Test
  public void matches()
  {
    // Matches - huzzah
    assertMatch( set( 0, 1 ), set( 2, 3 ), set( 4, 5 ), set( 0, 1, 2, 7 ) );

    // Matches and includes both ones
    assertMatch( set( 0, 1 ), set( 2, 3 ), set( 4, 5 ), set( 0, 1, 2, 3, 7 ) );

    // Missing all
    assertNoMatch( set( 0, 1 ), set( 2, 3 ), set( 4, 5 ), set( 1, 2, 7 ) );

    // Missing all one's
    assertNoMatch( set( 0, 1 ), set( 2, 3 ), set( 4, 5 ), set( 0, 1, 7 ) );

    // Includes exclude
    assertNoMatch( set( 0, 1 ), set( 2, 3 ), set( 4, 5 ), set( 0, 1, 2, 4, 7 ) );

    // Includes multiple excludes
    assertNoMatch( set( 0, 1 ), set( 2, 3 ), set( 4, 5 ), set( 0, 1, 2, 4, 5, 7 ) );
  }

  private void assertMatch( @Nonnull final BitSet all,
                            @Nonnull final BitSet one,
                            @Nonnull final BitSet exclude,
                            @Nonnull final BitSet componentIds )
  {
    _assertMatch( all, one, exclude, componentIds, true );
  }

  private void assertNoMatch( @Nonnull final BitSet all,
                              @Nonnull final BitSet one,
                              @Nonnull final BitSet exclude,
                              @Nonnull final BitSet componentIds )
  {
    _assertMatch( all, one, exclude, componentIds, false );
  }

  private void _assertMatch( @Nonnull final BitSet all,
                             @Nonnull final BitSet one,
                             @Nonnull final BitSet exclude,
                             @Nonnull final BitSet componentIds,
                             final boolean shouldMatch )
  {
    assertEquals( new AreaOfInterest( all, one, exclude ).matches( componentIds ), shouldMatch );
  }

  @Test
  public void toString_test()
  {
    final BitSet all = set( 0, 1, 5, 12 );
    final BitSet one = set( 2, 4 );
    final BitSet exclude = set( 14 );

    final AreaOfInterest areaOfInterest = new AreaOfInterest( all, one, exclude );

    assertEquals( areaOfInterest.toString(), "AreaOfInterest[all={0, 1, 5, 12},one={2, 4},exclude={14}]" );

    GaldrTestUtil.disableDebugToString();

    assertDefaultToString( areaOfInterest );
  }

  @Test
  public void hash_and_equals()
  {
    final BitSet set1 = set( 1 );
    final BitSet set2 = set( 2 );
    final BitSet set3 = set( 3 );
    final BitSet set4 = set( 4 );

    final AreaOfInterest aoi1 = new AreaOfInterest( set1, set2, set3 );
    final AreaOfInterest aoi2 = new AreaOfInterest( set1, set2, set3 );
    final AreaOfInterest aoi3 = new AreaOfInterest( set1, set2, set4 );

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
