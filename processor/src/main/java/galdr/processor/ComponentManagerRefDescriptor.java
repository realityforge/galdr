package galdr.processor;

import com.squareup.javapoet.ClassName;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.lang.model.element.ExecutableElement;

final class ComponentManagerRefDescriptor
{
  @Nonnull
  private final ExecutableElement _method;
  @Nonnull
  private final ClassName _componentType;

  ComponentManagerRefDescriptor( @Nonnull final ExecutableElement method, @Nonnull final ClassName componentType )
  {
    _method = Objects.requireNonNull( method );
    _componentType = Objects.requireNonNull( componentType );
  }

  @Nonnull
  ExecutableElement getMethod()
  {
    return _method;
  }

  @Nonnull
  ClassName getComponentType()
  {
    return _componentType;
  }
}
