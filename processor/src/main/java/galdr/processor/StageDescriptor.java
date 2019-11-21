package galdr.processor;

import com.squareup.javapoet.ClassName;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.lang.model.element.ExecutableElement;

final class StageDescriptor
{
  @Nonnull
  private final String _name;
  @Nonnull
  private final ExecutableElement _method;
  @Nonnull
  private List<ClassName> _subSystemTypes;

  StageDescriptor( @Nonnull final String name,
                   @Nonnull final ExecutableElement method,
                   @Nonnull final List<ClassName> subSystemTypes )
  {
    _name = Objects.requireNonNull( name );
    _method = Objects.requireNonNull( method );
    _subSystemTypes = Objects.requireNonNull( subSystemTypes );
  }

  @Nonnull
  String getName()
  {
    return _name;
  }

  @Nonnull
  ExecutableElement getMethod()
  {
    return _method;
  }

  @Nonnull
  List<ClassName> getSubSystemTypes()
  {
    return _subSystemTypes;
  }
}
