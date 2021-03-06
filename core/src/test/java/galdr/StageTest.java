package galdr;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class StageTest
  extends AbstractTest
{
  public static class MyProcessor
    implements SubSystem
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
    public void process( final int delta )
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
    public void process( final int delta )
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
      .stage( "MyStage" )
      .subSystem( "A", new MyProcessor( "A", trace ) )
      .subSystem( "B", new MyProcessor( "B", trace ) )
      .subSystem( "C", new MyProcessor( "C", trace ) )
      .endStage()
      .build();

    final Stage stage = world.getStageByName( "MyStage" );

    assertEquals( stage.getName(), "MyStage" );
    assertEquals( stage.toString(), "Stage[MyStage]" );

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
      .stage( "MyStage" )
      .subSystem( "A", new MyProcessor( "A", trace ) )
      .subSystem( "B", new MyProcessor( "B", trace ) )
      .subSystem( "C", new MyProcessor( "C", trace ) )
      .endStage()
      .build();

    final Stage stage = world.getStageByName( "MyStage" );
    assertEquals( stage.toString(), "Stage[MyStage]" );

    GaldrTestUtil.disableDebugToString();

    assertDefaultToString( stage );
  }

  @Test
  public void processorGeneratesError()
  {
    final List<String> trace = new ArrayList<>();
    final World world = Worlds.world( "MyWorld" )
      .stage( "MyStage" )
      .subSystem( "A", new MyProcessor( "A", trace ) )
      .subSystem( "B", new MyFaultyProcessor( "B", trace ) )
      .subSystem( "C", new MyProcessor( "C", trace ) )
      .endStage()
      .build();

    world.addErrorHandler( ( stage, processor, throwable ) ->
                             trace.add( "Stage: " + stage.getName() +
                                        ", Processor: " + processor +
                                        ", Error: " + throwable.toString() ) );
    final Stage stage = world.getStageByName( "MyStage" );

    assertEquals( stage.getName(), "MyStage" );
    assertEquals( stage.toString(), "Stage[MyStage]" );

    stage.process( 2 );

    assertEquals( String.join( ",", trace ),
                  "A:2,B:!!Error!!:2,Stage: MyStage, Processor: B, Error: java.lang.IllegalStateException: Blah!,C:2" );
  }
}
