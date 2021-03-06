package galdr.processor;

import javax.annotation.Nonnull;
import org.realityforge.proton.AbstractStandardProcessor;

abstract class AbstractGaldrProcessor
  extends AbstractStandardProcessor
{
  @Override
  @Nonnull
  protected final String getIssueTrackerURL()
  {
    return "https://github.com/realityforge/galdr/issues";
  }

  @Nonnull
  @Override
  protected final String getOptionPrefix()
  {
    return "galdr";
  }
}
