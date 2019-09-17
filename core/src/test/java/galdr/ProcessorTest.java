package galdr;

import javax.annotation.Nullable;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ProcessorTest
  extends AbstractTest
{
  public static class MyProcessor
    extends Processor
  {
    private int _lastDelta;

    MyProcessor()
    {
      super( "MyProcessor" );
    }

    @Override
    protected void process( final int delta )
    {
      _lastDelta = delta;
    }
  }

  public static class MyOtherProcessor
    extends Processor
  {
    MyOtherProcessor()
    {
      super( "MyOtherProcessor" );
    }

    @Override
    protected void process( final int delta )
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

    processor.process( 23 );

    assertEquals( processor._lastDelta, 23 );
  }
}
