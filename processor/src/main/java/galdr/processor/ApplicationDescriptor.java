package galdr.processor;

import javax.annotation.Nonnull;
import javax.lang.model.element.TypeElement;

final class ApplicationDescriptor
  extends AbstractDescriptor
{
  ApplicationDescriptor( @Nonnull final TypeElement element )
  {
    super( element );
  }
}
