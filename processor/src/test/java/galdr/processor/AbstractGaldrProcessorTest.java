package galdr.processor;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import org.realityforge.proton.qa.AbstractProcessorTest;

abstract class AbstractGaldrProcessorTest
  extends AbstractProcessorTest
{
  void assertSuccessfulCompile( @Nonnull final String classname )
    throws Exception
  {
    assertSuccessfulCompile( classname, deriveExpectedOutputs( classname ) );
  }

  @Nonnull
  private String[] deriveExpectedOutputs( @Nonnull final String classname )
  {
    final String[] elements = classname.contains( "." ) ? classname.split( "\\." ) : new String[]{ classname };
    final StringBuilder subsystem = new StringBuilder();
    subsystem.append( "expected" );
    for ( int i = 0; i < elements.length; i++ )
    {
      subsystem.append( '/' );
      final boolean isLastElement = i == elements.length - 1;
      if ( isLastElement )
      {
        subsystem.append( "Galdr_" );
      }
      subsystem.append( elements[ i ] );
    }
    subsystem.append( ".java" );
    final List<String> expectedOutputs = new ArrayList<>();
    expectedOutputs.add( subsystem.toString() );
    return expectedOutputs.toArray( new String[ 0 ] );
  }

  @Nonnull
  @Override
  protected String getOptionPrefix()
  {
    return "galdr";
  }
}
