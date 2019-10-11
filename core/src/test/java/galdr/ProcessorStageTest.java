package galdr;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ProcessorStageTest
  extends AbstractTest
{
  public static class MyProcessor
    extends Processor
  {
    @Nonnull
    final String _key;
    @Nonnull
    final List<String> _trace;

    MyProcessor( @Nonnull final String key, @Nonnull final List<String> trace )
    {
      _key = Objects.requireNonNull( key );
      _trace = Objects.requireNonNull( trace );
    }

    @Override
    protected void process( final int delta )
    {
      _trace.add( _key + ":" + delta );
      assertEquals( World.current().getName(), "MyWorld" );
    }
  }

  public static class MyFaultyProcessor
    extends MyProcessor
  {
    MyFaultyProcessor( @Nonnull final String key, @Nonnull final List<String> trace )
    {
      super( key, trace );
    }

    @Override
    protected void process( final int delta )
    {
      _trace.add( _key + ":!!Error!!:" + delta );
      throw new IllegalStateException( "Blah!" );
    }
  }

  @Test
  public void basicOperation()
  {
    final List<String> trace = new ArrayList<>();
    final World world = Worlds.world( "MyWorld" )
      .stage( "MyStage",
              new MyProcessor( "A", trace ),
              new MyProcessor( "B", trace ),
              new MyProcessor( "C", trace ) )
      .build();

    final ProcessorStage stage = world.getStageByName( "MyStage" );

    assertEquals( stage.getName(), "MyStage" );
    assertEquals( stage.toString(), "ProcessorStage[MyStage]" );

    stage.process( 2 );

    assertEquals( String.join( ",", trace ), "A:2,B:2,C:2" );

    stage.process( 3 );
    stage.process( 7 );

    assertEquals( String.join( ",", trace ), "A:2,B:2,C:2,A:3,B:3,C:3,A:7,B:7,C:7" );
  }

  @Test
  public void basicOperation_withArrayNotCopied()
  {
    GaldrTestUtil.disableCopyArraysPassedToConstructors();

    final List<String> trace = new ArrayList<>();
    final World world = Worlds.world( "MyWorld" )
      .stage( "MyStage",
              new MyProcessor( "A", trace ),
              new MyProcessor( "B", trace ),
              new MyProcessor( "C", trace ) )
      .build();

    final ProcessorStage stage = world.getStageByName( "MyStage" );

    assertEquals( stage.getName(), "MyStage" );
    assertEquals( stage.toString(), "ProcessorStage[MyStage]" );

    stage.process( 2 );

    assertEquals( String.join( ",", trace ), "A:2,B:2,C:2" );

    stage.process( 3 );
    stage.process( 7 );

    assertEquals( String.join( ",", trace ), "A:2,B:2,C:2,A:3,B:3,C:3,A:7,B:7,C:7" );
  }

  @Test
  public void toString_test()
  {
    final List<String> trace = new ArrayList<>();
    final World world = Worlds.world( "MyWorld" )
      .stage( "MyStage",
              new MyProcessor( "A", trace ),
              new MyProcessor( "B", trace ),
              new MyProcessor( "C", trace ) )
      .build();

    final ProcessorStage stage = world.getStageByName( "MyStage" );
    assertEquals( stage.toString(), "ProcessorStage[MyStage]" );

    GaldrTestUtil.disableDebugToString();

    assertDefaultToString( stage );
  }

  @Test
  public void processorGeneratesError()
  {
    final List<String> trace = new ArrayList<>();
    final MyFaultyProcessor faultyProcessor = new MyFaultyProcessor( "B", trace );
    final World world = Worlds.world( "MyWorld" )
      .stage( "MyStage",
              new MyProcessor( "A", trace ),
              faultyProcessor,
              new MyProcessor( "C", trace ) )
      .build();

    world.addErrorHandler( ( stage, processor, throwable ) ->
                             trace.add( "Stage: " + stage.getName() +
                                        ", Processor: " + processor +
                                        ", Error: " + throwable.toString() ) );
    final ProcessorStage stage = world.getStageByName( "MyStage" );

    assertEquals( stage.getName(), "MyStage" );
    assertEquals( stage.toString(), "ProcessorStage[MyStage]" );

    stage.process( 2 );

    assertEquals( String.join( ",", trace ),
                  "A:2,B:!!Error!!:2,Stage: MyStage, Processor: " + faultyProcessor +
                  ", Error: java.lang.IllegalStateException: Blah!,C:2" );
  }
}
