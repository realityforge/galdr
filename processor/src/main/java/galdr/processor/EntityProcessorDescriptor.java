package galdr.processor;

import com.squareup.javapoet.TypeName;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.lang.model.element.ExecutableElement;

final class EntityProcessorDescriptor
{
  @Nonnull
  private final ExecutableElement _method;
  @Nonnull
  private final List<TypeName> _all;
  @Nonnull
  private final List<TypeName> _one;
  @Nonnull
  private final List<TypeName> _exclude;

  EntityProcessorDescriptor( @Nonnull final ExecutableElement method,
                             @Nonnull final List<TypeName> all,
                             @Nonnull final List<TypeName> one,
                             @Nonnull final List<TypeName> exclude )
  {
    _method = Objects.requireNonNull( method );
    _all = Objects.requireNonNull( all );
    _one = Objects.requireNonNull( one );
    _exclude = Objects.requireNonNull( exclude );
  }

  @Nonnull
  ExecutableElement getMethod()
  {
    return _method;
  }

  @Nonnull
  List<TypeName> getAll()
  {
    return _all;
  }

  @Nonnull
  List<TypeName> getOne()
  {
    return _one;
  }

  @Nonnull
  List<TypeName> getExclude()
  {
    return _exclude;
  }
}
