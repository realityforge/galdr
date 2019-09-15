package galdr;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ProcessorTest
  extends AbstractTest
{
  public static class MyProcessor
    extends Processor
  {
    private float _lastTimeDelta;

    @Override
    public void process( final float timeDelta )
    {
      _lastTimeDelta = timeDelta;
    }
  }

  public static class MyOtherProcessor
    extends Processor
  {
    @Override
    public void process( final float timeDelta )
    {
    }
  }

  @Test
  public void construct()
  {
    final Processor processor = new MyProcessor();

    final World world = WorldBuilder.create().build();

    processor.setWorld( world );

    assertEquals( processor.getWorld(), world );
    assertEquals( processor.getName(), "MyProcessor" );
    assertEquals( processor.toString(), "Processor[MyProcessor]" );
  }

  @Test
  public void process()
  {
    final MyProcessor processor = new MyProcessor();

    final World world = WorldBuilder.create().build();
    processor.setWorld( world );

    processor.process( 23F );

    assertEquals( processor._lastTimeDelta, 23F );
  }

  @Test
  public void toString_test()
  {
    final Processor processor = new MyProcessor();

    assertEquals( processor.toString(), "Processor[MyProcessor]" );

    GaldrTestUtil.disableDebugToString();

    assertNotEquals( processor.toString(), "Processor[MyProcessor]" );
  }

  @Test
  public void getName()
  {
    final Processor processor = new MyOtherProcessor();

    assertEquals( processor.getName(), "MyOtherProcessor" );

    GaldrTestUtil.disableNames();

    assertInvariantFailure( processor::getName,
                            "Galdr-0004: Processor.getName() invoked when Galdr.areNamesEnabled() returns false" );
  }
}
