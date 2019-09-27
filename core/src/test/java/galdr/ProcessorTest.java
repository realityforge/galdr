package galdr;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ProcessorTest
  extends AbstractTest
{
  public static class MyProcessor
    extends Processor
  {
    private int _lastDelta;

    @Override
    protected void process( final int delta )
    {
      _lastDelta = delta;
    }
  }

  @Test
  public void construct()
  {
    final Worlds.Builder builder = Worlds.world();
    // Cache the processor in var
    final Processor processor = new MyProcessor();
    builder.stage( "MyStage", processor ).build();

    assertEquals( processor.getName(), "MyProcessor" );
    assertEquals( processor.toString(), "Processor[MyProcessor]" );
  }

  @Test
  public void process()
  {
    final MyProcessor processor = new MyProcessor();

    processor.process( 23 );

    assertEquals( processor._lastDelta, 23 );
  }

  @Test
  public void world()
  {
    final MyProcessor processor = new MyProcessor();

    assertInvariantFailure( processor::world, "Galdr-0026: Invoked WorldHolder.world() when no world was active." );

    final World world = Worlds.world().build();
    WorldHolder.activateWorld( world );
    assertEquals( processor.world(), world );
  }
}
