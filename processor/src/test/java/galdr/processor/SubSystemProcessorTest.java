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
        new Object[]{ "com.example.component_manager_ref.Suppressed1ProtectedAccessComponentManagerRefSubSystem" },
        new Object[]{ "com.example.component_manager_ref.Suppressed2PublicAccessComponentManagerRefSubSystem" },
        new Object[]{ "com.example.component_manager_ref.Suppressed2ProtectedAccessComponentManagerRefSubSystem" },

        new Object[]{ "com.example.entity_processor.BasicEntityProcessorSubSystem" },
        new Object[]{ "com.example.entity_processor.ComplexAreaOfInterest1EntityProcessorSubSystem" },
        new Object[]{ "com.example.entity_processor.ComplexAreaOfInterest2EntityProcessorSubSystem" },
        new Object[]{ "com.example.entity_processor.ComplexAreaOfInterest3EntityProcessorSubSystem" },
        new Object[]{ "com.example.entity_processor.ComplexAreaOfInterest4EntityProcessorSubSystem" },
        new Object[]{ "com.example.entity_processor.DeltaParameterEntityProcessorSubSystem" },
        new Object[]{ "com.example.entity_processor.MultiEntityProcessorSubSystem" },
        new Object[]{ "com.example.entity_processor.PackageAccessEntityProcessorSubSystem" },
        new Object[]{ "com.example.entity_processor.Suppressed1PublicAccessEntityProcessorSubSystem" },
        new Object[]{ "com.example.entity_processor.Suppressed1ProtectedAccessEntityProcessorSubSystem" },
        new Object[]{ "com.example.entity_processor.Suppressed2PublicAccessEntityProcessorSubSystem" },
        new Object[]{ "com.example.entity_processor.Suppressed2ProtectedAccessEntityProcessorSubSystem" },

        new Object[]{ "com.example.ctor.PackageAccessCtorSubSystem" },
        new Object[]{ "com.example.ctor.PublicAccessCtorSubSystem" },

        new Object[]{ "com.example.name_ref.BasicNameRefSubSystem" },
        new Object[]{ "com.example.name_ref.MultiNameRefSubSystem" },
        new Object[]{ "com.example.name_ref.PackageAccessNameRefSubSystem" },
        new Object[]{ "com.example.name_ref.Suppressed1PublicAccessNameRefSubSystem" },
        new Object[]{ "com.example.name_ref.Suppressed1ProtectedAccessNameRefSubSystem" },
        new Object[]{ "com.example.name_ref.Suppressed2PublicAccessNameRefSubSystem" },
        new Object[]{ "com.example.name_ref.Suppressed2ProtectedAccessNameRefSubSystem" },

        new Object[]{ "com.example.on_activate.BasicOnActivateSubSystem" },
        new Object[]{ "com.example.on_activate.MultiOnActivateSubSystem" },
        new Object[]{ "com.example.on_activate.PackageAccessOnActivateSubSystem" },
        new Object[]{ "com.example.on_activate.Suppressed1PublicAccessOnActivateSubSystem" },
        new Object[]{ "com.example.on_activate.Suppressed1ProtectedAccessOnActivateSubSystem" },
        new Object[]{ "com.example.on_activate.Suppressed2PublicAccessOnActivateSubSystem" },
        new Object[]{ "com.example.on_activate.Suppressed2ProtectedAccessOnActivateSubSystem" },

        new Object[]{ "com.example.on_deactivate.BasicOnDeactivateSubSystem" },
        new Object[]{ "com.example.on_deactivate.MultiOnDeactivateSubSystem" },
        new Object[]{ "com.example.on_deactivate.PackageAccessOnDeactivateSubSystem" },
        new Object[]{ "com.example.on_deactivate.Suppressed1PublicAccessOnDeactivateSubSystem" },
        new Object[]{ "com.example.on_deactivate.Suppressed1ProtectedAccessOnDeactivateSubSystem" },
        new Object[]{ "com.example.on_deactivate.Suppressed2PublicAccessOnDeactivateSubSystem" },
        new Object[]{ "com.example.on_deactivate.Suppressed2ProtectedAccessOnDeactivateSubSystem" },

        new Object[]{ "com.example.processor.BasicProcessorSubSystem" },
        new Object[]{ "com.example.processor.MultiProcessorSubSystem" },
        new Object[]{ "com.example.processor.PackageAccessProcessorSubSystem" },
        new Object[]{ "com.example.processor.ParameterizedProcessorSubSystem" },
        new Object[]{ "com.example.processor.Suppressed1PublicAccessProcessorSubSystem" },
        new Object[]{ "com.example.processor.Suppressed1ProtectedAccessProcessorSubSystem" },
        new Object[]{ "com.example.processor.Suppressed2PublicAccessProcessorSubSystem" },
        new Object[]{ "com.example.processor.Suppressed2ProtectedAccessProcessorSubSystem" },

        new Object[]{ "com.example.world_ref.BasicWorldRefSubSystem" },
        new Object[]{ "com.example.world_ref.MultiWorldRefSubSystem" },
        new Object[]{ "com.example.world_ref.PackageAccessWorldRefSubSystem" },
        new Object[]{ "com.example.world_ref.Suppressed1ProtectedAccessWorldRefSubSystem" },
        new Object[]{ "com.example.world_ref.Suppressed1ProtectedAccessWorldRefSubSystem" },
        new Object[]{ "com.example.world_ref.Suppressed2ProtectedAccessWorldRefSubSystem" },
        new Object[]{ "com.example.world_ref.Suppressed2ProtectedAccessWorldRefSubSystem" },

        new Object[]{ "com.example.BasicSubSystem" },
        new Object[]{ "com.example.CompleteSubSystem" },
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
      "@ComponentManagerRef target should not be public. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Galdr:PublicRefMethod\" ) or @SuppressGaldrWarnings( \"Galdr:PublicRefMethod\" )";
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:-processing", "-implicit:none" ).
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
      "@ComponentManagerRef target should not be protected. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Galdr:ProtectedRefMethod\" ) or @SuppressGaldrWarnings( \"Galdr:ProtectedRefMethod\" )";
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:-processing", "-implicit:none" ).
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

  @Test
  public void publicAccessViaInterfaceComponentManagerRef()
    throws Exception
  {
    final String input1 =
      toFilename( "input", "com.example.component_manager_ref.PublicAccessViaInterfaceComponentManagerRefSubSystem" );
    final String input2 = toFilename( "input", "com.example.component_manager_ref.ComponentManagerRefInterface" );
    final String output =
      toFilename( "expected",
                  "com.example.component_manager_ref.Galdr_PublicAccessViaInterfaceComponentManagerRefSubSystem" );
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void publicAccessEntityProcessor()
  {
    final String filename =
      toFilename( "input", "com.example.entity_processor.PublicAccessEntityProcessorSubSystem" );
    final String messageFragment =
      "@EntityProcessor target should not be public. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Galdr:PublicLifecycleMethod\" ) or @SuppressGaldrWarnings( \"Galdr:PublicLifecycleMethod\" )";
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:-processing", "-implicit:none" ).
      processedWith( new SubSystemProcessor() ).
      compilesWithoutError().
      withWarningCount( 1 ).
      withWarningContaining( messageFragment );
  }

  @Test
  public void protectedAccessEntityProcessor()
  {
    final String filename =
      toFilename( "input", "com.example.entity_processor.ProtectedAccessEntityProcessorSubSystem" );
    final String messageFragment =
      "@EntityProcessor target should not be protected. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Galdr:ProtectedLifecycleMethod\" ) or @SuppressGaldrWarnings( \"Galdr:ProtectedLifecycleMethod\" )";
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:-processing", "-implicit:none" ).
      processedWith( new SubSystemProcessor() ).
      compilesWithoutError().
      withWarningCount( 1 ).
      withWarningContaining( messageFragment );
  }

  @Test
  public void validProtectedAccessEntityProcessor()
    throws Exception
  {
    final String input1 =
      toFilename( "input", "com.example.entity_processor.ProtectedAccessFromBaseEntityProcessorSubSystem" );
    final String input2 =
      toFilename( "input", "com.example.entity_processor.other.BaseProtectedAccessEntityProcessorSubSystem" );
    final String output =
      toFilename( "expected",
                  "com.example.entity_processor.Galdr_ProtectedAccessFromBaseEntityProcessorSubSystem" );
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void publicAccessViaInterfaceEntityProcessor()
    throws Exception
  {
    final String input1 =
      toFilename( "input", "com.example.entity_processor.PublicAccessViaInterfaceEntityProcessorSubSystem" );
    final String input2 = toFilename( "input", "com.example.entity_processor.EntityProcessorInterface" );
    final String output =
      toFilename( "expected",
                  "com.example.entity_processor.Galdr_PublicAccessViaInterfaceEntityProcessorSubSystem" );
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void publicAccessNameRef()
  {
    final String filename =
      toFilename( "input", "com.example.name_ref.PublicAccessNameRefSubSystem" );
    final String messageFragment =
      "@NameRef target should not be public. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Galdr:PublicRefMethod\" ) or @SuppressGaldrWarnings( \"Galdr:PublicRefMethod\" )";
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:-processing", "-implicit:none" ).
      processedWith( new SubSystemProcessor() ).
      compilesWithoutError().
      withWarningCount( 1 ).
      withWarningContaining( messageFragment );
  }

  @Test
  public void protectedAccessNameRef()
  {
    final String filename =
      toFilename( "input", "com.example.name_ref.ProtectedAccessNameRefSubSystem" );
    final String messageFragment =
      "@NameRef target should not be protected. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Galdr:ProtectedRefMethod\" ) or @SuppressGaldrWarnings( \"Galdr:ProtectedRefMethod\" )";
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:-processing", "-implicit:none" ).
      processedWith( new SubSystemProcessor() ).
      compilesWithoutError().
      withWarningCount( 1 ).
      withWarningContaining( messageFragment );
  }

  @Test
  public void validProtectedAccessNameRef()
    throws Exception
  {
    final String input1 = toFilename( "input", "com.example.name_ref.ProtectedAccessFromBaseNameRefSubSystem" );
    final String input2 = toFilename( "input", "com.example.name_ref.other.BaseProtectedAccessNameRefSubSystem" );
    final String output =
      toFilename( "expected", "com.example.name_ref.Galdr_ProtectedAccessFromBaseNameRefSubSystem" );
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void publicAccessViaInterfaceWorldRef()
    throws Exception
  {
    final String input1 = toFilename( "input", "com.example.world_ref.PublicAccessViaInterfaceWorldRefSubSystem" );
    final String input2 = toFilename( "input", "com.example.world_ref.WorldRefInterface" );
    final String output =
      toFilename( "expected", "com.example.world_ref.Galdr_PublicAccessViaInterfaceWorldRefSubSystem" );
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void publicAccessOnActivate()
  {
    final String filename =
      toFilename( "input", "com.example.on_activate.PublicAccessOnActivateSubSystem" );
    final String messageFragment =
      "@OnActivate target should not be public. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Galdr:PublicLifecycleMethod\" ) or @SuppressGaldrWarnings( \"Galdr:PublicLifecycleMethod\" )";
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:-processing", "-implicit:none" ).
      processedWith( new SubSystemProcessor() ).
      compilesWithoutError().
      withWarningCount( 1 ).
      withWarningContaining( messageFragment );
  }

  @Test
  public void protectedAccessOnActivate()
  {
    final String filename =
      toFilename( "input", "com.example.on_activate.ProtectedAccessOnActivateSubSystem" );
    final String messageFragment =
      "@OnActivate target should not be protected. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Galdr:ProtectedLifecycleMethod\" ) or @SuppressGaldrWarnings( \"Galdr:ProtectedLifecycleMethod\" )";
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:-processing", "-implicit:none" ).
      processedWith( new SubSystemProcessor() ).
      compilesWithoutError().
      withWarningCount( 1 ).
      withWarningContaining( messageFragment );
  }

  @Test
  public void validProtectedAccessOnActivate()
    throws Exception
  {
    final String input1 = toFilename( "input", "com.example.on_activate.ProtectedAccessFromBaseOnActivateSubSystem" );
    final String input2 = toFilename( "input", "com.example.on_activate.other.BaseProtectedAccessOnActivateSubSystem" );
    final String output =
      toFilename( "expected", "com.example.on_activate.Galdr_ProtectedAccessFromBaseOnActivateSubSystem" );
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void publicAccessViaInterfaceOnActivateRef()
    throws Exception
  {
    final String input1 = toFilename( "input", "com.example.on_activate.PublicAccessViaInterfaceOnActivateSubSystem" );
    final String input2 = toFilename( "input", "com.example.on_activate.OnActivateInterface" );
    final String output =
      toFilename( "expected", "com.example.on_activate.Galdr_PublicAccessViaInterfaceOnActivateSubSystem" );
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void publicAccessOnDeactivate()
  {
    final String filename =
      toFilename( "input", "com.example.on_deactivate.PublicAccessOnDeactivateSubSystem" );
    final String messageFragment =
      "@OnDeactivate target should not be public. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Galdr:PublicLifecycleMethod\" ) or @SuppressGaldrWarnings( \"Galdr:PublicLifecycleMethod\" )";
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:-processing", "-implicit:none" ).
      processedWith( new SubSystemProcessor() ).
      compilesWithoutError().
      withWarningCount( 1 ).
      withWarningContaining( messageFragment );
  }

  @Test
  public void protectedAccessOnDeactivate()
  {
    final String filename =
      toFilename( "input", "com.example.on_deactivate.ProtectedAccessOnDeactivateSubSystem" );
    final String messageFragment =
      "@OnDeactivate target should not be protected. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Galdr:ProtectedLifecycleMethod\" ) or @SuppressGaldrWarnings( \"Galdr:ProtectedLifecycleMethod\" )";
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:-processing", "-implicit:none" ).
      processedWith( new SubSystemProcessor() ).
      compilesWithoutError().
      withWarningCount( 1 ).
      withWarningContaining( messageFragment );
  }

  @Test
  public void validProtectedAccessOnDeactivate()
    throws Exception
  {
    final String input1 =
      toFilename( "input", "com.example.on_deactivate.ProtectedAccessFromBaseOnDeactivateSubSystem" );
    final String input2 =
      toFilename( "input", "com.example.on_deactivate.other.BaseProtectedAccessOnDeactivateSubSystem" );
    final String output =
      toFilename( "expected", "com.example.on_deactivate.Galdr_ProtectedAccessFromBaseOnDeactivateSubSystem" );
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void publicAccessViaInterfaceOnDeactivateRef()
    throws Exception
  {
    final String input1 =
      toFilename( "input", "com.example.on_deactivate.PublicAccessViaInterfaceOnDeactivateSubSystem" );
    final String input2 = toFilename( "input", "com.example.on_deactivate.OnDeactivateInterface" );
    final String output =
      toFilename( "expected", "com.example.on_deactivate.Galdr_PublicAccessViaInterfaceOnDeactivateSubSystem" );
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void publicAccessProcessor()
  {
    final String filename =
      toFilename( "input", "com.example.processor.PublicAccessProcessorSubSystem" );
    final String messageFragment =
      "@Processor target should not be public. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Galdr:PublicLifecycleMethod\" ) or @SuppressGaldrWarnings( \"Galdr:PublicLifecycleMethod\" )";
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:-processing", "-implicit:none" ).
      processedWith( new SubSystemProcessor() ).
      compilesWithoutError().
      withWarningCount( 1 ).
      withWarningContaining( messageFragment );
  }

  @Test
  public void protectedAccessProcessor()
  {
    final String filename =
      toFilename( "input", "com.example.processor.ProtectedAccessProcessorSubSystem" );
    final String messageFragment =
      "@Processor target should not be protected. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Galdr:ProtectedLifecycleMethod\" ) or @SuppressGaldrWarnings( \"Galdr:ProtectedLifecycleMethod\" )";
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:-processing", "-implicit:none" ).
      processedWith( new SubSystemProcessor() ).
      compilesWithoutError().
      withWarningCount( 1 ).
      withWarningContaining( messageFragment );
  }

  @Test
  public void validProtectedAccessProcessor()
    throws Exception
  {
    final String input1 =
      toFilename( "input", "com.example.processor.ProtectedAccessFromBaseProcessorSubSystem" );
    final String input2 =
      toFilename( "input", "com.example.processor.other.BaseProtectedAccessProcessorSubSystem" );
    final String output =
      toFilename( "expected", "com.example.processor.Galdr_ProtectedAccessFromBaseProcessorSubSystem" );
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void publicAccessViaInterfaceProcessorRef()
    throws Exception
  {
    final String input1 =
      toFilename( "input", "com.example.processor.PublicAccessViaInterfaceProcessorSubSystem" );
    final String input2 = toFilename( "input", "com.example.processor.ProcessorInterface" );
    final String output =
      toFilename( "expected", "com.example.processor.Galdr_PublicAccessViaInterfaceProcessorSubSystem" );
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void publicAccessWorldRef()
  {
    final String fileworld =
      toFilename( "input", "com.example.world_ref.PublicAccessWorldRefSubSystem" );
    final String messageFragment =
      "@WorldRef target should not be public. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Galdr:PublicRefMethod\" ) or @SuppressGaldrWarnings( \"Galdr:PublicRefMethod\" )";
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( fileworld ) ) ).
      withCompilerOptions( "-Xlint:-processing", "-implicit:none" ).
      processedWith( new SubSystemProcessor() ).
      compilesWithoutError().
      withWarningCount( 1 ).
      withWarningContaining( messageFragment );
  }

  @Test
  public void protectedAccessWorldRef()
  {
    final String fileworld =
      toFilename( "input", "com.example.world_ref.ProtectedAccessWorldRefSubSystem" );
    final String messageFragment =
      "@WorldRef target should not be protected. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Galdr:ProtectedRefMethod\" ) or @SuppressGaldrWarnings( \"Galdr:ProtectedRefMethod\" )";
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( fileworld ) ) ).
      withCompilerOptions( "-Xlint:-processing", "-implicit:none" ).
      processedWith( new SubSystemProcessor() ).
      compilesWithoutError().
      withWarningCount( 1 ).
      withWarningContaining( messageFragment );
  }

  @Test
  public void validProtectedAccessWorldRef()
    throws Exception
  {
    final String input1 = toFilename( "input", "com.example.world_ref.ProtectedAccessFromBaseWorldRefSubSystem" );
    final String input2 = toFilename( "input", "com.example.world_ref.other.BaseProtectedAccessWorldRefSubSystem" );
    final String output =
      toFilename( "expected", "com.example.world_ref.Galdr_ProtectedAccessFromBaseWorldRefSubSystem" );
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void publicAccessViaInterfaceNameRef()
    throws Exception
  {
    final String input1 = toFilename( "input", "com.example.name_ref.PublicAccessViaInterfaceNameRefSubSystem" );
    final String input2 = toFilename( "input", "com.example.name_ref.NameRefInterface" );
    final String output =
      toFilename( "expected", "com.example.name_ref.Galdr_PublicAccessViaInterfaceNameRefSubSystem" );
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void completeViaBaseClassSubSystem()
    throws Exception
  {
    final String input1 = toFilename( "input", "com.example.CompleteViaBaseClassSubSystem" );
    final String input2 = toFilename( "input", "com.example.other.CompleteBaseSubSystem" );
    final String output = toFilename( "expected", "com.example.Galdr_CompleteViaBaseClassSubSystem" );
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void completeViaInterfaceSubSystem()
    throws Exception
  {
    final String input1 = toFilename( "input", "com.example.CompleteViaInterfaceSubSystem" );
    final String input2 = toFilename( "input", "com.example.other.CompleteInterfaceSubSystem" );
    final String output = toFilename( "expected", "com.example.Galdr_CompleteViaInterfaceSubSystem" );
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
        new Object[]{ "com.example.component_manager_ref.VoidComponentManagerRefSubSystem",
                      "@ComponentManagerRef target must return a value" },

        new Object[]{ "com.example.entity_processor.AbstractEntityProcessorSubSystem",
                      "@EntityProcessor target must not be abstract" },
        new Object[]{ "com.example.entity_processor.NoAreaOfInterestEntityProcessorSubSystem",
                      "@EntityProcessor target must specify at least one component in the 'all', 'one' or 'exclude' requirements" },
        new Object[]{ "com.example.entity_processor.NotParameterizedEntityProcessorSubSystem",
                      "@EntityProcessor target must have one or two integer parameters" },
        new Object[]{ "com.example.entity_processor.OverlapAllAllEntityProcessorSubSystem",
                      "@EntityProcessor target contains the component of type com.example.entity_processor.OverlapAllAllEntityProcessorSubSystem.MyComponent multiple times in the 'all' requirement" },
        new Object[]{ "com.example.entity_processor.OverlapAllExcludeEntityProcessorSubSystem",
                      "@EntityProcessor target contains the component of type com.example.entity_processor.OverlapAllExcludeEntityProcessorSubSystem.MyComponent in the 'all' requirement and the 'exclude' requirement" },
        new Object[]{ "com.example.entity_processor.OverlapAllOneEntityProcessorSubSystem",
                      "@EntityProcessor target contains the component of type com.example.entity_processor.OverlapAllOneEntityProcessorSubSystem.MyComponent in the 'all' requirement and the 'one' requirement" },
        new Object[]{ "com.example.entity_processor.OverlapExcludeExcludeEntityProcessorSubSystem",
                      "@EntityProcessor target contains the component of type com.example.entity_processor.OverlapExcludeExcludeEntityProcessorSubSystem.MyComponent multiple times in the 'exclude' requirement" },
        new Object[]{ "com.example.entity_processor.OverlapOneExcludeEntityProcessorSubSystem",
                      "@EntityProcessor target contains the component of type com.example.entity_processor.OverlapOneExcludeEntityProcessorSubSystem.MyComponent in the 'one' requirement and the 'exclude' requirement" },
        new Object[]{ "com.example.entity_processor.OverlapOneOneEntityProcessorSubSystem",
                      "@EntityProcessor target contains the component of type com.example.entity_processor.OverlapOneOneEntityProcessorSubSystem.MyComponent multiple times in the 'one' requirement" },
        new Object[]{ "com.example.entity_processor.ParameterizedTooManyEntityProcessorSubSystem",
                      "@EntityProcessor target must have one or two integer parameters" },
        new Object[]{ "com.example.entity_processor.PrivateEntityProcessorSubSystem",
                      "@EntityProcessor target must not be private" },
        new Object[]{ "com.example.entity_processor.ReturnsValueEntityProcessorSubSystem",
                      "@EntityProcessor target must not return a value" },
        new Object[]{ "com.example.entity_processor.SingleOneRequirementEntityProcessorSubSystem",
                      "@EntityProcessor target must have multiple components in the 'one' requirement or alternatively the component should be moved to the 'all' requirement" },
        new Object[]{ "com.example.entity_processor.StaticEntityProcessorSubSystem",
                      "@EntityProcessor target must not be static" },
        new Object[]{ "com.example.entity_processor.ThrowsExceptionEntityProcessorSubSystem",
                      "@EntityProcessor target must not throw any exceptions" },

        new Object[]{ "com.example.ctor.MultipleCtorSubSystem", "@SubSystem target has more than one constructor" },
        new Object[]{ "com.example.ctor.PrivateAccessCtorSubSystem", "@SubSystem target has a private constructor" },

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
        new Object[]{ "com.example.name_ref.VoidNameRefSubSystem", "@NameRef target must return a value" },

        new Object[]{ "com.example.on_activate.AbstractOnActivateSubSystem",
                      "@OnActivate target must not be abstract" },
        new Object[]{ "com.example.on_activate.ParameterizedOnActivateSubSystem",
                      "@OnActivate target must not have any parameters" },
        new Object[]{ "com.example.on_activate.PrivateOnActivateSubSystem", "@OnActivate target must not be private" },
        new Object[]{ "com.example.on_activate.ReturnsValueOnActivateSubSystem",
                      "@OnActivate target must not return a value" },
        new Object[]{ "com.example.on_activate.StaticOnActivateSubSystem", "@OnActivate target must not be static" },
        new Object[]{ "com.example.on_activate.ThrowsExceptionOnActivateSubSystem",
                      "@OnActivate target must not throw any exceptions" },

        new Object[]{ "com.example.on_deactivate.AbstractOnDeactivateSubSystem",
                      "@OnDeactivate target must not be abstract" },
        new Object[]{ "com.example.on_deactivate.ParameterizedOnDeactivateSubSystem",
                      "@OnDeactivate target must not have any parameters" },
        new Object[]{ "com.example.on_deactivate.PrivateOnDeactivateSubSystem",
                      "@OnDeactivate target must not be private" },
        new Object[]{ "com.example.on_deactivate.ReturnsValueOnDeactivateSubSystem",
                      "@OnDeactivate target must not return a value" },
        new Object[]{ "com.example.on_deactivate.StaticOnDeactivateSubSystem",
                      "@OnDeactivate target must not be static" },
        new Object[]{ "com.example.on_deactivate.ThrowsExceptionOnDeactivateSubSystem",
                      "@OnDeactivate target must not throw any exceptions" },

        new Object[]{ "com.example.processor.AbstractProcessorSubSystem",
                      "@Processor target must not be abstract" },
        new Object[]{ "com.example.processor.ParameterizedBadCountProcessorSubSystem",
                      "@Processor target must have zero parameters or a single integer parameter" },
        new Object[]{ "com.example.processor.ParameterizedBadTypeProcessorSubSystem",
                      "@Processor target must have zero parameters or a single integer parameter" },
        new Object[]{ "com.example.processor.PrivateProcessorSubSystem",
                      "@Processor target must not be private" },
        new Object[]{ "com.example.processor.ReturnsValueProcessorSubSystem",
                      "@Processor target must not return a value" },
        new Object[]{ "com.example.processor.StaticProcessorSubSystem",
                      "@Processor target must not be static" },
        new Object[]{ "com.example.processor.ThrowsExceptionProcessorSubSystem",
                      "@Processor target must not throw any exceptions" },

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
        new Object[]{ "com.example.world_ref.VoidWorldRefSubSystem", "@WorldRef target must return a value" },

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

  @Test
  public void unreachableComponentManagerRefSubSystem()
  {
    final String input1 =
      toFilename( "bad_input", "com.example.component_manager_ref.UnreachableComponentManagerRefSubSystem" );
    final String input2 =
      toFilename( "bad_input", "com.example.component_manager_ref.other.BaseUnreachableComponentManagerRefSubSystem" );
    assertFailedCompileResource( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                                 "@ComponentManagerRef target must not be package access if the method is in a different package from the type annotated with the @GaldrApplication annotation" );
  }

  @Test
  public void unreachableEntityProcessorSubSystem()
  {
    final String input1 = toFilename( "bad_input", "com.example.entity_processor.UnreachableEntityProcessorSubSystem" );
    final String input2 =
      toFilename( "bad_input", "com.example.entity_processor.other.BaseUnreachableEntityProcessorSubSystem" );
    assertFailedCompileResource( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                                 "@EntityProcessor target must not be package access if the method is in a different package from the type annotated with the @GaldrApplication annotation" );
  }

  @Test
  public void unreachableNameRefSubSystem()
  {
    final String input1 = toFilename( "bad_input", "com.example.name_ref.UnreachableNameRefSubSystem" );
    final String input2 = toFilename( "bad_input", "com.example.name_ref.other.BaseUnreachableNameRefSubSystem" );
    assertFailedCompileResource( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                                 "@NameRef target must not be package access if the method is in a different package from the type annotated with the @GaldrApplication annotation" );
  }

  @Test
  public void unreachableOnActivateSubSystem()
  {
    final String input1 = toFilename( "bad_input", "com.example.on_activate.UnreachableOnActivateSubSystem" );
    final String input2 = toFilename( "bad_input", "com.example.on_activate.other.BaseUnreachableOnActivateSubSystem" );
    assertFailedCompileResource( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                                 "@OnActivate target must not be package access if the method is in a different package from the type annotated with the @GaldrApplication annotation" );
  }

  @Test
  public void unreachableOnDeactivateSubSystem()
  {
    final String input1 = toFilename( "bad_input", "com.example.on_deactivate.UnreachableOnDeactivateSubSystem" );
    final String input2 =
      toFilename( "bad_input", "com.example.on_deactivate.other.BaseUnreachableOnDeactivateSubSystem" );
    assertFailedCompileResource( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                                 "@OnDeactivate target must not be package access if the method is in a different package from the type annotated with the @GaldrApplication annotation" );
  }

  @Test
  public void unreachableProcessorSubSystem()
  {
    final String input1 = toFilename( "bad_input", "com.example.processor.UnreachableProcessorSubSystem" );
    final String input2 = toFilename( "bad_input", "com.example.processor.other.BaseUnreachableProcessorSubSystem" );
    assertFailedCompileResource( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                                 "@Processor target must not be package access if the method is in a different package from the type annotated with the @GaldrApplication annotation" );
  }

  @Test
  public void unreachableWorldRefSubSystem()
  {
    final String input1 = toFilename( "bad_input", "com.example.world_ref.UnreachableWorldRefSubSystem" );
    final String input2 = toFilename( "bad_input", "com.example.world_ref.other.BaseUnreachableWorldRefSubSystem" );
    assertFailedCompileResource( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                                 "@WorldRef target must not be package access if the method is in a different package from the type annotated with the @GaldrApplication annotation" );
  }
}
