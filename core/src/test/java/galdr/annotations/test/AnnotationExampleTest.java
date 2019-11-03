package galdr.annotations.test;

import galdr.AbstractTest;
import galdr.ProcessorStage;
import org.testng.annotations.Test;

public class AnnotationExampleTest
  extends AbstractTest
{
  @Test
  public void basicOperation()
  {
    // This is a very basic test that is used to try out the developer experience of
    // using the annotation development driven model
    final MyApplication application = MyApplication.create();

    final ProcessorStage stage = application.sim();
    stage.process( 1 );
    stage.process( 1 );
    stage.process( 1 );
    stage.process( 1 );
  }
}
