package galdr.integration;

import galdr.GaldrTestUtil;
import org.realityforge.braincheck.BrainCheckTestUtil;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class AbstractTest
{
  @BeforeMethod
  protected void beforeTest()
  {
    BrainCheckTestUtil.resetConfig( false );
    GaldrTestUtil.resetConfig( false );
  }

  @AfterMethod
  protected void afterTest()
  {
    GaldrTestUtil.resetConfig( true );
    BrainCheckTestUtil.resetConfig( true );
  }
}
