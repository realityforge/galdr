package galdr;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * An ordered list of {@link Processor} instances that are explicitly invoked by application code.
 */
public final class ProcessorStage
  extends Element
{
  @Nonnull
  private final Processor[] _processors;

  ProcessorStage( @Nullable final String name, @Nonnull final Processor... processors )
  {
    super( name );
    if ( Galdr.shouldCopyArraysPassedToConstructors() )
    {
      _processors = new Processor[ processors.length ];
      for ( int i = 0; i < _processors.length; i++ )
      {
        _processors[ i ] = Objects.requireNonNull( processors[ i ] );
      }
    }
    else
    {
      _processors = processors;
    }
  }

  public void process( final int delta )
  {
    for ( final Processor processor : _processors )
    {
      try
      {
        processor.process( delta );
      }
      catch ( final Throwable e )
      {
        //TODO: Deliver error to per stage or world-global error handler?
      }
    }
  }

  @Nonnull
  @Override
  protected final String getBaseTypeName()
  {
    return "ProcessorStage";
  }
}
