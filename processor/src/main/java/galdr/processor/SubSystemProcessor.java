package galdr.processor;

import java.io.IOException;
import javax.annotation.Nonnull;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

/**
 * Annotation processor that generates subsystem implementations.
 */
@SupportedAnnotationTypes( Constants.SUB_SYSTEM_CLASSNAME )
@SupportedSourceVersion( SourceVersion.RELEASE_8 )
public final class SubSystemProcessor
  extends AbstractGaldrProcessor
{
  @Override
  @Nonnull
  String getRootAnnotationClassname()
  {
    return Constants.SUB_SYSTEM_CLASSNAME;
  }

  @Override
  final void process( @Nonnull final TypeElement element )
    throws IOException, GaldrProcessorException
  {
  }
}
