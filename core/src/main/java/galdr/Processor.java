package galdr;

/**
 * Base processor class.
 */
public abstract class Processor
{
  protected abstract void process( int delta );

  @Override
  public String toString()
  {
    if ( Galdr.areDebugToStringMethodsEnabled() )
    {
      return "Processor[" + getClass().getSimpleName() + "]";
    }
    else
    {
      return super.toString();
    }
  }
}
