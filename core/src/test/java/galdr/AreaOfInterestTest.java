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
}
