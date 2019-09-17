package galdr;

import javax.annotation.Nonnull;

/**
 * Base processor class.
 */
public abstract class Processor
  extends WorldElement
{
  protected abstract void process( int delta );

  @Nonnull
  @Override
  protected final String getBaseTypeName()
  {
    return "Processor";
  }
}
