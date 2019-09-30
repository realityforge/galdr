package galdr;

import java.util.BitSet;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class AreaOfInterestTest
  extends AbstractTest
{
  @Test
  public void construct()
  {
    final BitSet all = new BitSet();
    final BitSet one = new BitSet();
    final BitSet exclude = new BitSet();

    final AreaOfInterest areaOfInterest = new AreaOfInterest( all, one, exclude );

    assertEquals( areaOfInterest.getAll(), all );
    assertEquals( areaOfInterest.getOne(), one );
    assertEquals( areaOfInterest.getExclude(), exclude );
  }

  @Test
  public void construct_overlappingBitsets()
  {
    final BitSet set0 = new BitSet();
    final BitSet set1 = new BitSet();
    final BitSet set2 = new BitSet();

    set1.set( 0 );
    set1.set( 1 );
    set1.set( 2 );

    set2.set( 2 );
    set2.set( 3 );
    set2.set( 4 );

    assertInvariantFailure( () -> new AreaOfInterest( set1, set2, set0 ),
                            "Galdr-0005: AreaOfInterest passed intersecting BitSets all ({0, 1, 2}) and one ({2, 3, 4})." );
    assertInvariantFailure( () -> new AreaOfInterest( set1, set0, set2 ),
                            "Galdr-0005: AreaOfInterest passed intersecting BitSets all ({0, 1, 2}) and exclude ({2, 3, 4})." );
    assertInvariantFailure( () -> new AreaOfInterest( set0, set1, set2 ),
                            "Galdr-0005: AreaOfInterest passed intersecting BitSets one ({0, 1, 2}) and exclude ({2, 3, 4})." );
  }

  @Test
  public void toString_test()
  {
    final BitSet all = new BitSet( 16 );
    final BitSet one = new BitSet( 16 );
    final BitSet exclude = new BitSet( 16 );

    all.set( 0 );
    all.set( 1 );
    all.set( 5 );
    all.set( 12 );

    one.set( 2 );
    all.set( 4 );

    exclude.set( 14 );

    final AreaOfInterest areaOfInterest = new AreaOfInterest( all, one, exclude );

    assertEquals( areaOfInterest.toString(), "AreaOfInterest[all={0, 1, 4, 5, 12},one={2},exclude={14}]" );

    GaldrTestUtil.disableDebugToString();

    assertDefaultToString( areaOfInterest );
  }

  @Test
  public void hash_and_equals()
  {
    final BitSet set1 = new BitSet();
    final BitSet set2 = new BitSet();
    final BitSet set3 = new BitSet();
    final BitSet set4 = new BitSet();

    set1.set( 1 );
    set2.set( 2 );
    set3.set( 3 );
    set4.set( 4 );

    final AreaOfInterest aoi1 = new AreaOfInterest( set1, set2, set3 );
    final AreaOfInterest aoi2 = new AreaOfInterest( set1, set2, set3 );
    final AreaOfInterest aoi3 = new AreaOfInterest( set1, set2, set4 );

    assertEquals( aoi1.hashCode(), aoi1.hashCode() );
    assertEquals( aoi1.hashCode(), aoi2.hashCode() );
    assertNotEquals( aoi1.hashCode(), aoi3.hashCode() );

    assertEquals( aoi1, aoi1 );
    assertEquals( aoi1, aoi2 );
    assertNotEquals( aoi1, aoi3 );
  }
}
