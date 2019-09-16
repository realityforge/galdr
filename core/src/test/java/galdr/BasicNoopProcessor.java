package galdr;

import javax.annotation.Nonnull;

final class BasicNoopProcessor
  extends Processor
{
  BasicNoopProcessor()
  {
    this( "BasicNoopProcessor" );
  }

  BasicNoopProcessor( @Nonnull final String name )
  {
    super( Galdr.areNamesEnabled() ? name : null );
  }

  @Override
  protected void process( final int delta )
  {
  }
}
