package galdr.processor;

import com.squareup.javapoet.ClassName;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

final class SubSystemDescriptor
{
  @Nonnull
  private final TypeElement _element;
  @Nonnull
  private final String _name;
  private final int _priority;
  @Nonnull
  private final List<ExecutableElement> _nameRefs = new ArrayList<>();

  SubSystemDescriptor( @Nonnull final TypeElement element, @Nonnull final String name, final int priority )
  {
    _element = Objects.requireNonNull( element );
    _name = Objects.requireNonNull( name );
    _priority = priority;
  }

  @Nonnull
  TypeElement getElement()
  {
    return _element;
  }

  @Nonnull
  String getName()
  {
    return _name;
  }

  int getPriority()
  {
    return _priority;
  }

  @Nonnull
  String getPackageName()
  {
    return GeneratorUtil.getQualifiedPackageName( getElement() );
  }

  @Nonnull
  ClassName getEnhancedClassName()
  {
    return Generator.toGeneratedClassName( getElement() );
  }

  void addNameRef( @Nonnull final ExecutableElement method )
  {
    _nameRefs.add( Objects.requireNonNull( method ) );
  }

  @Nonnull
  List<ExecutableElement> getNameRefs()
  {
    return _nameRefs;
  }
}
