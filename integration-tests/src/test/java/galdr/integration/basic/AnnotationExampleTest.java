package galdr.integration.basic;

import galdr.Stage;
import galdr.integration.AbstractTest;
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

    final Stage stage = application.sim();
    stage.process( 1 );
    stage.process( 1 );
    stage.process( 1 );
    stage.process( 1 );
  }
}
