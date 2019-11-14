package galdr.processor;

import com.google.testing.compile.JavaSourcesSubjectFactory;
import java.util.Arrays;
import java.util.Collections;
import javax.annotation.Nonnull;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static com.google.common.truth.Truth.*;

public class SubSystemProcessorTest
  extends AbstractProcessorTest
{
  @DataProvider( name = "successfulCompiles" )
  public Object[][] successfulCompiles()
  {
    return new Object[][]
      {
        new Object[]{ "com.example.component_manager_ref.BasicComponentManagerRefSubSystem" },
        new Object[]{ "com.example.component_manager_ref.MultiComponentManagerRefSubSystem" },
        new Object[]{ "com.example.component_manager_ref.MultiSameComponentManagerRefSubSystem" },
        new Object[]{ "com.example.component_manager_ref.PackageAccessComponentManagerRefSubSystem" },
        new Object[]{ "com.example.component_manager_ref.Suppressed1PublicAccessComponentManagerRefSubSystem" },
        new Object[]{ "com.example.component_manager_ref.Suppressed2PublicAccessComponentManagerRefSubSystem" },

        new Object[]{ "com.example.ctor.PackageAccessCtorSubSystem" },
        new Object[]{ "com.example.ctor.PublicAccessCtorSubSystem" },

        new Object[]{ "com.example.name_ref.BasicNameRefSubSystem" },
        new Object[]{ "com.example.name_ref.MultiNameRefSubSystem" },
        new Object[]{ "com.example.name_ref.PublicAccessNameRefSubSystem" },
        new Object[]{ "com.example.name_ref.ProtectedAccessNameRefSubSystem" },
        new Object[]{ "com.example.name_ref.PackageAccessNameRefSubSystem" },

        new Object[]{ "com.example.world_ref.BasicWorldRefSubSystem" },
        new Object[]{ "com.example.world_ref.MultiWorldRefSubSystem" },
        new Object[]{ "com.example.world_ref.PublicAccessWorldRefSubSystem" },
        new Object[]{ "com.example.world_ref.ProtectedAccessWorldRefSubSystem" },
        new Object[]{ "com.example.world_ref.PackageAccessWorldRefSubSystem" },

        new Object[]{ "com.example.BasicSubSystem" },
        new Object[]{ "com.example.PackageAccessSubSystem" },

        new Object[]{ "DefaultPackageSubSystem" }
      };
  }

  @Test( dataProvider = "successfulCompiles" )
  public void processSuccessfulCompile( @Nonnull final String classname )
    throws Exception
  {
    assertSuccessfulCompile( classname );
  }

  @Test
  public void staticInnerClassSubSystem()
    throws Exception
  {
    assertSuccessfulCompile( "input/com/example/StaticInnerClassSubSystem.java",
                             "expected/com/example/StaticInnerClassSubSystem_Galdr_Foo.java" );
  }

  @Test
  public void publicAccessComponentManagerRef()
  {
    final String filename =
      toFilename( "input", "com.example.component_manager_ref.PublicAccessComponentManagerRefSubSystem" );
    final String messageFragment =
      "@ComponentManagerRef target should not be public. This warning can be suppressed by annotating the element with @SuppressWarnings( \\\"Galdr:PublicRefMethod\\\" ) or @SuppressGaldrWarnings( \\\"Galdr:PublicRefMethod\\\" )";
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:-processing", "-implicit:class" ).
      processedWith( new SubSystemProcessor() ).
      compilesWithoutError().
      withWarningCount( 1 ).
      withWarningContaining( messageFragment );
  }

  @Test
  public void protectedAccessComponentManagerRef()
  {
    final String filename =
      toFilename( "input", "com.example.component_manager_ref.ProtectedAccessComponentManagerRefSubSystem" );
    final String messageFragment =
      "@ComponentManagerRef target should not be protected. This warning can be suppressed by annotating the element with @SuppressWarnings( \\\"Galdr:ProtectedRefMethod\\\" ) or @SuppressGaldrWarnings( \\\"Galdr:ProtectedRefMethod\\\" )";
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:-processing", "-implicit:class" ).
      processedWith( new SubSystemProcessor() ).
      compilesWithoutError().
      withWarningCount( 1 ).
      withWarningContaining( messageFragment );
  }

  @Test
  public void validProtectedAccessComponentManagerRef()
    throws Exception
  {
    final String input1 =
      toFilename( "input", "com.example.component_manager_ref.ProtectedAccessFromBaseComponentManagerRefSubSystem" );
    final String input2 =
      toFilename( "input", "com.example.component_manager_ref.other.BaseProtectedAccessComponentManagerRefSubSystem" );
    final String output =
      toFilename( "expected",
                  "com.example.component_manager_ref.Galdr_ProtectedAccessFromBaseComponentManagerRefSubSystem" );
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @DataProvider( name = "failedCompiles" )
  public Object[][] failedCompiles()
  {
    return new Object[][]
      {
        new Object[]{ "com.example.component_manager_ref.BadType1ComponentManagerRefSubSystem",
                      "@ComponentManagerRef target must return the type galdr.ComponentManager parameterized with the desired component type" },
        new Object[]{ "com.example.component_manager_ref.BadType2ComponentManagerRefSubSystem",
                      "@ComponentManagerRef target must return the type galdr.ComponentManager parameterized with the desired component type" },
        new Object[]{ "com.example.component_manager_ref.BadType3ComponentManagerRefSubSystem",
                      "@ComponentManagerRef target must return the type galdr.ComponentManager parameterized with the desired component type" },
        new Object[]{ "com.example.component_manager_ref.BadType4ComponentManagerRefSubSystem",
                      "@ComponentManagerRef target must return the type galdr.ComponentManager parameterized with the desired component type" },
        new Object[]{ "com.example.component_manager_ref.BadType5ComponentManagerRefSubSystem",
                      "@ComponentManagerRef target must return the type galdr.ComponentManager parameterized with the desired component type" },
        new Object[]{ "com.example.component_manager_ref.ConcreteComponentManagerRefSubSystem",
                      "@ComponentManagerRef target must be abstract" },
        new Object[]{ "com.example.component_manager_ref.ParameterizedComponentManagerRefSubSystem",
                      "@ComponentManagerRef target must not have any parameters" },
        new Object[]{ "com.example.component_manager_ref.PrivateComponentManagerRefSubSystem",
                      "@ComponentManagerRef target must be abstract" },
        new Object[]{ "com.example.component_manager_ref.StaticComponentManagerRefSubSystem",
                      "@ComponentManagerRef target must be abstract" },
        new Object[]{ "com.example.component_manager_ref.ThrowsExceptionComponentManagerRefSubSystem",
                      "@ComponentManagerRef target must not throw any exceptions" },
        new Object[]{ "com.example.component_manager_ref.UnreachableComponentManagerRefSubSystem",
                      "@ComponentManagerRef target must not be package access if the method is in a different package from the type annotated with the @GaldrApplication annotation" },
        new Object[]{ "com.example.component_manager_ref.VoidComponentManagerRefSubSystem",
                      "@ComponentManagerRef target must return a value" },

        new Object[]{ "com.example.ctor.MultipleCtorSubSystem", "@SubSystem target has more than one constructor" },
        new Object[]{ "com.example.ctor.PrivateAccessCtorSubSystem", "@SubSystem target has a private constructor" },

        new Object[]{ "com.example.world_ref.BadType1WorldRefSubSystem",
                      "@WorldRef target must return an instance of galdr.World" },
        new Object[]{ "com.example.world_ref.BadType2WorldRefSubSystem",
                      "@WorldRef target must return an instance of galdr.World" },
        new Object[]{ "com.example.world_ref.BadType3WorldRefSubSystem",
                      "@WorldRef target must return an instance of galdr.World" },
        new Object[]{ "com.example.world_ref.BadType4WorldRefSubSystem",
                      "@WorldRef target must return an instance of galdr.World" },
        new Object[]{ "com.example.world_ref.ConcreteWorldRefSubSystem", "@WorldRef target must be abstract" },
        new Object[]{ "com.example.world_ref.ParameterizedWorldRefSubSystem",
                      "@WorldRef target must not have any parameters" },
        new Object[]{ "com.example.world_ref.PrivateWorldRefSubSystem", "@WorldRef target must be abstract" },
        new Object[]{ "com.example.world_ref.StaticWorldRefSubSystem", "@WorldRef target must be abstract" },
        new Object[]{ "com.example.world_ref.ThrowsExceptionWorldRefSubSystem",
                      "@WorldRef target must not throw any exceptions" },
        new Object[]{ "com.example.world_ref.UnreachableWorldRefSubSystem",
                      "@WorldRef target must not be package access if the method is in a different package from the type annotated with the @GaldrApplication annotation" },
        new Object[]{ "com.example.world_ref.VoidWorldRefSubSystem", "@WorldRef target must return a value" },

        new Object[]{ "com.example.name_ref.BadType1NameRefSubSystem",
                      "@NameRef target must return an instance of java.lang.String" },
        new Object[]{ "com.example.name_ref.BadType2NameRefSubSystem",
                      "@NameRef target must return an instance of java.lang.String" },
        new Object[]{ "com.example.name_ref.BadType3NameRefSubSystem",
                      "@NameRef target must return an instance of java.lang.String" },
        new Object[]{ "com.example.name_ref.BadType4NameRefSubSystem",
                      "@NameRef target must return an instance of java.lang.String" },
        new Object[]{ "com.example.name_ref.ConcreteNameRefSubSystem", "@NameRef target must be abstract" },
        new Object[]{ "com.example.name_ref.ParameterizedNameRefSubSystem",
                      "@NameRef target must not have any parameters" },
        new Object[]{ "com.example.name_ref.PrivateNameRefSubSystem", "@NameRef target must be abstract" },
        new Object[]{ "com.example.name_ref.StaticNameRefSubSystem", "@NameRef target must be abstract" },
        new Object[]{ "com.example.name_ref.ThrowsExceptionNameRefSubSystem",
                      "@NameRef target must not throw any exceptions" },
        new Object[]{ "com.example.name_ref.UnreachableNameRefSubSystem",
                      "@NameRef target must not be package access if the method is in a different package from the type annotated with the @GaldrApplication annotation" },
        new Object[]{ "com.example.name_ref.VoidNameRefSubSystem", "@NameRef target must return a value" },

        new Object[]{ "com.example.ConcreteSubSystem", "@SubSystem target must be abstract" },
        new Object[]{ "com.example.EnumSubSystem", "@SubSystem target must be a class" },
        new Object[]{ "com.example.InterfaceSubSystem", "@SubSystem target must be a class" },
        new Object[]{ "com.example.NonStaticInnerClassSubSystem",
                      "@SubSystem target must not be a non-static nested class" }
      };
  }

  @Test( dataProvider = "failedCompiles" )
  public void processFailedCompile( @Nonnull final String classname, @Nonnull final String errorMessageFragment )
  {
    assertFailedCompile( classname, errorMessageFragment );
  }
}
