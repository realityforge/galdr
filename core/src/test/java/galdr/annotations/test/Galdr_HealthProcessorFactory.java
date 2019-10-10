package galdr.annotations.test;

import galdr.Processor;
import java.util.function.Supplier;
import javax.annotation.Generated;

@Generated( "galdr" )
public final class Galdr_HealthProcessorFactory
  implements Supplier<Processor>
{
  @Override
  public Processor get()
  {
    return new Galdr_HealthProcessor();
  }
}
