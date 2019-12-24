package galdr.processor;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.lang.model.element.TypeElement;

final class ApplicationDescriptor
  extends AbstractDescriptor
{
  @Nonnull
  private final List<ComponentDescriptor> _components = new ArrayList<>();
  @Nonnull
  private final List<StageDescriptor> _stages = new ArrayList<>();

  ApplicationDescriptor( @Nonnull final TypeElement element )
  {
    super( element );
  }

  void addComponent( @Nonnull final ComponentDescriptor component )
  {
    _components.add( component );
  }

  @Nonnull
  List<ComponentDescriptor> getComponents()
  {
    return _components;
  }

  void addStage( @Nonnull final StageDescriptor stage )
  {
    _stages.add( stage );
  }

  @Nonnull
  List<StageDescriptor> getStages()
  {
    return _stages;
  }
}
