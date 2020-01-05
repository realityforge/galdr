package galdr.processor;

import javax.annotation.Nonnull;
import org.realityforge.proton.qa.AbstractProcessorTest;

abstract class AbstractGaldrProcessorTest
  extends AbstractProcessorTest
{
  void assertSuccessfulCompile( @Nonnull final String classname )
    throws Exception
  {
    assertSuccessfulCompile( classname, toFilename( "expected", classname, "Galdr_", ".java" ) );
  }

  @Nonnull
  @Override
  protected String getOptionPrefix()
  {
    return "galdr";
  }
}
