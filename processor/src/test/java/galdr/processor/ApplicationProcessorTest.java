package galdr.processor;

import java.util.Arrays;
import java.util.Collections;
import javax.annotation.Nonnull;
import javax.annotation.processing.Processor;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ApplicationProcessorTest
  extends AbstractGaldrProcessorTest
{
  @DataProvider( name = "successfulCompiles" )
  public Object[][] successfulCompiles()
  {
    return new Object[][]
      {
        new Object[]{ "com.example.component.AutodetectArrayComponentTypeApplication" },
        new Object[]{ "com.example.component.AutodetectFlagComponentTypeApplication" },
        new Object[]{ "com.example.component.ExplicitArrayComponentTypeApplication" },
        new Object[]{ "com.example.component.ExplicitAutodetectComponentTypeApplication" },
        new Object[]{ "com.example.component.ExplicitFlagComponentTypeApplication" },
        new Object[]{ "com.example.component.ExplicitMapComponentTypeApplication" },
        new Object[]{ "com.example.component.MultipleComponentApplication" },

        new Object[]{ "com.example.stage.CustomNameStage" },
        new Object[]{ "com.example.stage.NonStandardMethodNameStage" },

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
    assertSuccessfulCompile( "com.example.StaticInnerClassApplication",
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
        new Object[]{ "com.example.component.BadComponentType1Application",
                      "@GaldrApplication target must have a components parameter that references classes annotated by @Component but references the type com.example.component.BadComponentType1Application.MyComponent that is not annotated appropriately" },
        new Object[]{ "com.example.component.BadComponentType2Application",
                      "@GaldrApplication target must have a components parameter that references classes annotated by @Component but references the type java.lang.String that is not annotated appropriately" },
        new Object[]{ "com.example.component.BadComponentType3Application",
                      "@GaldrApplication target must have a components parameter that references classes annotated by @Component but references the type int that is not annotated appropriately" },
        new Object[]{ "com.example.component.DuplicateComponentTypeApplication",
                      "@GaldrApplication target must not have a duplicate type in the components parameter. The type com.example.component.DuplicateComponentTypeApplication.MyComponent1 appears multiple times" },
        new Object[]{ "com.example.component.NoComponentApplication",
                      "@GaldrApplication target must have at least one Component defined by the components parameter" },

        new Object[]{ "com.example.stage.BadName1Stage",
                      "@GaldrStage target specified an invalid value '-1-' for the parameter name. The value must be a valid java identifier" },
        new Object[]{ "com.example.stage.BadName2Stage",
                      "@GaldrStage target specified an invalid value 'int' for the parameter name. The value must not be a java keyword" },
        new Object[]{ "com.example.stage.BadSubSystemType1Stage",
                      "@GaldrStage target must have a value parameter that references classes annotated by @GaldrSubSystem but references the type com.example.stage.BadSubSystemType1Stage.MySubSystem that is not annotated appropriately" },
        new Object[]{ "com.example.stage.BadSubSystemType2Stage",
                      "@GaldrStage target must have a value parameter that references classes annotated by @GaldrSubSystem but references the type java.lang.String that is not annotated appropriately" },
        new Object[]{ "com.example.stage.BadSubSystemType3Stage",
                      "@GaldrStage target must have a value parameter that references classes annotated by @GaldrSubSystem but references the type int that is not annotated appropriately" },
        new Object[]{ "com.example.stage.DuplicateNameStage",
                      "@GaldrStage target must not have the same name as any other stage but the stage defined by the method named sim has the same name" },
        new Object[]{ "com.example.stage.DuplicateSubSystemTypeStage",
                      "@GaldrStage target must not have a duplicate type in the value parameter. The type com.example.stage.DuplicateSubSystemTypeStage.MySubSystem1 appears multiple times" },
        new Object[]{ "com.example.stage.NoSubSystemTypeStage",
                      "@GaldrStage target must have at least one SubSystem defined by the value parameter" },

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
  protected String getFixtureKeyPart()
  {
    return ".application";
  }

  @Nonnull
  @Override
  protected Processor processor()
  {
    return new ApplicationProcessor();
  }

  @Nonnull
  @Override
  protected Processor[] additionalProcessors()
  {
    return new Processor[]{new SubSystemProcessor()};
  }
}
