package galdr.processor;

import java.io.IOException;
import javax.annotation.Nonnull;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

/**
 * Annotation processor that generates application implementations.
 */
@SupportedAnnotationTypes( Constants.GALDR_APPLICATION_CLASSNAME )
@SupportedSourceVersion( SourceVersion.RELEASE_8 )
public final class ApplicationProcessor
  extends AbstractGaldrProcessor
{
  @Override
  @Nonnull
  String getRootAnnotationClassname()
  {
    return Constants.GALDR_APPLICATION_CLASSNAME;
  }

  @Override
  final void process( @Nonnull final TypeElement element )
    throws IOException, ProcessorException
  {
  }
}
