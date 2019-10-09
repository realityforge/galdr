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
    }

    MyProcessor( @Nullable final String name )
    {
      super( name );
    }

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
    builder.stage( randomString(), processor ).build();

    assertEquals( processor.getName(), "MyProcessor" );
  }

  @Test
  public void construct_namedPassedButNamesDisabled()
  {
    GaldrTestUtil.disableNames();
    assertInvariantFailure( () -> new MyProcessor( "MyName" ),
                            "Galdr-0052: Processor passed a name 'MyName' but Galdr.areNamesEnabled() is false" );
  }

  @Test
  public void toString_test()
  {
    final Worlds.Builder builder = Worlds.world();
    // Cache the processor in var
    final Processor processor = new MyProcessor();
    builder.stage( randomString(), processor ).build();

    assertEquals( processor.toString(), "Processor[MyProcessor]" );

    GaldrTestUtil.disableDebugToString();

    assertDefaultToString( processor );
  }

  @Test
  public void getName()
  {
    final Worlds.Builder builder = Worlds.world();
    // Cache the processor in var
    final Processor processor = new MyProcessor();
    builder.stage( randomString(), processor ).build();

    assertEquals( processor.getName(), "MyProcessor" );

    GaldrTestUtil.disableNames();

    assertInvariantFailure( processor::getName,
                            "Galdr-0004: Processor.getName() invoked when Galdr.areNamesEnabled() returns false" );
  }

  @Test
  public void process()
  {
    final MyProcessor processor = new MyProcessor();

    processor.process( 23 );

    assertEquals( processor._lastDelta, 23 );
  }
}
