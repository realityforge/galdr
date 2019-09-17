package galdr;

import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class WorldElementTest
  extends AbstractTest
{
  static final class MyElement
    extends WorldElement
  {
    @Nonnull
    @Override
    protected String getBaseTypeName()
    {
      return "MyElement";
    }
  }

  @Test
  public void basicOperation()
  {
    final MyElement element = new MyElement();

    assertEquals( element.getName(), "MyElement" );

    assertInvariantFailure( element::world, "Galdr-0026: Invoked WorldHolder.world() when no world was active." );

    final World world = Galdr.world().build();
    WorldHolder.activateWorld( world );
    assertEquals( element.world(), world );
  }
}
