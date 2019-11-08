package galdr.processor;

import javax.annotation.Nonnull;

abstract class BaseGaldrProcessor
  extends AbstractGaldrProcessor
{
  @Override
  @Nonnull
  final String getIssueTrackerURL()
  {
    return "https://github.com/realityforge/galdr/issues";
  }

  @Nonnull
  @Override
  final String getOptionPrefix()
  {
    return "galdr";
  }
}
