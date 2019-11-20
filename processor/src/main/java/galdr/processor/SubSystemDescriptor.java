package galdr.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

final class SubSystemDescriptor
  extends AbstractDescriptor
{
  @Nonnull
  private final String _name;
  @Nonnull
  private final List<ComponentManagerRefDescriptor> _componentManagerRefs = new ArrayList<>();
  @Nonnull
  private final List<EntityProcessorDescriptor> _entityProcessors = new ArrayList<>();
  @Nonnull
  private final List<ExecutableElement> _nameRefs = new ArrayList<>();
  @Nonnull
  private final List<ExecutableElement> _onActivates = new ArrayList<>();
  @Nonnull
  private final List<ExecutableElement> _onDeactivates = new ArrayList<>();
  @Nonnull
  private final List<ExecutableElement> _processors = new ArrayList<>();
  @Nonnull
  private final List<ExecutableElement> _worldRefs = new ArrayList<>();

  SubSystemDescriptor( @Nonnull final TypeElement element, @Nonnull final String name )
  {
    super( element );
    _name = Objects.requireNonNull( name );
  }

  @Nonnull
  String getName()
  {
    return _name;
  }

  void addComponentManagerRef( @Nonnull final ExecutableElement method, @Nonnull final ClassName componentType )
  {
    _componentManagerRefs.add( new ComponentManagerRefDescriptor( method, componentType ) );
  }

  @Nonnull
  List<ComponentManagerRefDescriptor> getComponentManagerRefs()
  {
    return _componentManagerRefs;
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

  void addEntityProcessor( @Nonnull final ExecutableElement method,
                           @Nonnull final List<TypeName> all,
                           @Nonnull final List<TypeName> one,
                           @Nonnull final List<TypeName> exclude )
  {
    _entityProcessors.add( new EntityProcessorDescriptor( method, all, one, exclude ) );
  }

  @Nonnull
  List<EntityProcessorDescriptor> getEntityProcessors()
  {
    return _entityProcessors;
  }

  void addOnActivate( @Nonnull final ExecutableElement method )
  {
    _onActivates.add( Objects.requireNonNull( method ) );
  }

  @Nonnull
  List<ExecutableElement> getOnActivates()
  {
    return _onActivates;
  }

  void addOnDeactivate( @Nonnull final ExecutableElement method )
  {
    _onDeactivates.add( Objects.requireNonNull( method ) );
  }

  @Nonnull
  List<ExecutableElement> getOnDeactivates()
  {
    return _onDeactivates;
  }

  void addProcessor( @Nonnull final ExecutableElement method )
  {
    _processors.add( Objects.requireNonNull( method ) );
  }

  @Nonnull
  List<ExecutableElement> getProcessors()
  {
    return _processors;
  }

  void addWorldRef( @Nonnull final ExecutableElement method )
  {
    _worldRefs.add( Objects.requireNonNull( method ) );
  }

  @Nonnull
  List<ExecutableElement> getWorldRefs()
  {
    return _worldRefs;
  }
}
