package galdr.processor;

import javax.annotation.Nonnull;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SubSystemProcessorTest
  extends AbstractProcessorTest
{
  @DataProvider( name = "successfulCompiles" )
  public Object[][] successfulCompiles()
  {
    return new Object[][]
      {
        new Object[]{ "com.example.ctor.PackageAccessCtorSubSystem" },
        new Object[]{ "com.example.ctor.PublicAccessCtorSubSystem" },
        new Object[]{ "com.example.name_ref.BasicNameRefSubSystem" },
        new Object[]{ "com.example.name_ref.MultiNameRefSubSystem" },
        new Object[]{ "com.example.name_ref.PublicAccessNameRefSubSystem" },
        new Object[]{ "com.example.name_ref.PackageAccessNameRefSubSystem" },
        new Object[]{ "com.example.BasicSubSystem" },
        new Object[]{ "com.example.PackageAccessSubSystem" },
        new Object[]{ "com.example.StaticInnerClassSubSystem" },
        new Object[]{ "DefaultPackageSubSystem" }
      };
  }

  @Test( dataProvider = "successfulCompiles" )
  public void processSuccessfulCompile( @Nonnull final String classname )
    throws Exception
  {
    assertSuccessfulCompile( classname );
  }

  @DataProvider( name = "failedCompiles" )
  public Object[][] failedCompiles()
  {
    return new Object[][]
      {
        new Object[]{ "com.example.ctor.MultipleCtorSubSystem", "XXXXXXXX" },
        new Object[]{ "com.example.ctor.PrivateAccessCtorSubSystem", "XXXXXXXX" },
        new Object[]{ "com.example.name_ref.ConcreteNameRefSubSystem", "@NameRef target must be abstract" },
        new Object[]{ "com.example.name_ref.ParameterizedNameRefSubSystem", "@NameRef target must not have any parameters" },
        new Object[]{ "com.example.name_ref.PrivateNameRefSubSystem", "@NameRef target must be abstract" },
        new Object[]{ "com.example.name_ref.StaticNameRefSubSystem", "@NameRef target must be abstract" },
        new Object[]{ "com.example.name_ref.ThrowsExceptionNameRefSubSystem", "@NameRef target must not throw any exceptions" },
        new Object[]{ "com.example.name_ref.UnreachableNameRefSubSystem", "@NameRef target must not be package access if the method is in a different package from the @GaldrApplication" },
        new Object[]{ "com.example.ConcreteSubSystem", "@SubSystem target must be abstract" },
        new Object[]{ "com.example.EnumSubSystem", "@SubSystem target must be a class" },
        new Object[]{ "com.example.InterfaceSubSystem", "@SubSystem target must be a class" },
        new Object[]{ "com.example.NonStaticInnerClassSubSystem", "@SubSystem target must not be a non-static nested class" },
      };
  }

  @Test( dataProvider = "failedCompiles" )
  public void processFailedCompile( @Nonnull final String classname, @Nonnull final String errorMessageFragment )
  {
    assertFailedCompile( classname, errorMessageFragment );
  }
}
