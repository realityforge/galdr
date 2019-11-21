package galdr.processor;

import java.util.Arrays;
import java.util.Collections;
import javax.annotation.Nonnull;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ApplicationProcessorTest
  extends AbstractProcessorTest
{
  @DataProvider( name = "successfulCompiles" )
  public Object[][] successfulCompiles()
  {
    return new Object[][]
      {
        new Object[]{ "com.example.BasicApplication" },
        new Object[]{ "com.example.CompleteApplication" },
        new Object[]{ "com.example.PackageAccessApplication" },
        new Object[]{ "com.example.PublicAccessApplication" },

        new Object[]{ "DefaultPackageApplication" }
      };
  }

  @Test( dataProvider = "successfulCompiles" )
  public void processSuccessfulCompile( @Nonnull final String classname )
    throws Exception
  {
    assertSuccessfulCompile( classname );
  }

  @Test
  public void staticInnerClassApplication()
    throws Exception
  {
    assertSuccessfulCompile( "input/com/example/StaticInnerClassApplication.java",
                             "expected/com/example/StaticInnerClassApplication_Galdr_BasicApplication.java" );
  }

  @Test
  public void completeViaBaseClassSubSystem()
    throws Exception
  {
    final String input1 = toFilename( "input", "com.example.CompleteViaBaseClassApplication" );
    final String input2 = toFilename( "input", "com.example.other.CompleteBaseApplication" );
    final String output = toFilename( "expected", "com.example.Galdr_CompleteViaBaseClassApplication" );
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void completeViaInterfaceSubSystem()
    throws Exception
  {
    final String input1 = toFilename( "input", "com.example.CompleteViaInterfaceApplication" );
    final String input2 = toFilename( "input", "com.example.other.CompleteInterfaceApplication" );
    final String output = toFilename( "expected", "com.example.Galdr_CompleteViaInterfaceApplication" );
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @DataProvider( name = "failedCompiles" )
  public Object[][] failedCompiles()
  {
    return new Object[][]
      {
        new Object[]{ "com.example.ConcreteApplication", "@GaldrApplication target must be abstract" },
        new Object[]{ "com.example.EnumApplication", "@GaldrApplication target must be a class" },
        new Object[]{ "com.example.InterfaceApplication", "@GaldrApplication target must be a class" },
        new Object[]{ "com.example.NonStaticInnerClassApplication",
                      "@GaldrApplication target must not be a non-static nested class" }
      };
  }

  @Test( dataProvider = "failedCompiles" )
  public void processFailedCompile( @Nonnull final String classname, @Nonnull final String errorMessageFragment )
  {
    assertFailedCompile( classname, errorMessageFragment );
  }

  @Nonnull
  @Override
  ProcessorType processorType()
  {
    return ProcessorType.APPLICATION;
  }
}
