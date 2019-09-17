package galdr;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ElementTest
  extends AbstractTest
{
  private static class MyElement
    extends Element
  {
    MyElement()
    {
    }

    MyElement( @Nullable final String name )
    {
      super( name );
    }

    @Nonnull
    @Override
    protected String getBaseTypeName()
    {
      return "TestElement";
    }
  }

  @Test
  public void basicOperation()
  {
    final MyElement element = new MyElement();
    assertEquals( element.getName(), "MyElement" );
  }

  @Test
  public void toString_test()
  {
    final MyElement element = new MyElement();

    assertEquals( element.toString(), "TestElement[MyElement]" );

    GaldrTestUtil.disableDebugToString();

    assertNotEquals( element.toString(), "TestElement[MyElement]" );
  }

  @Test
  public void getName()
  {
    assertEquals( new MyElement().getName(), "MyElement" );
    assertEquals( new MyElement( "MyName" ).getName(), "MyName" );

    GaldrTestUtil.disableNames();

    final MyElement element = new MyElement();
    assertInvariantFailure( element::getName,
                            "Galdr-0004: Element.getName() invoked when Galdr.areNamesEnabled() returns false" );
  }

  @Test
  public void constructWithNameWhenNamesDisabled()
  {
    GaldrTestUtil.disableNames();

    assertInvariantFailure( () -> new MyElement( "MyName" ),
                            "Galdr-0052: Element passed a name 'MyName' but Galdr.areNamesEnabled() is false" );
  }
}
