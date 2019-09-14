package galdr;

import java.util.Set;
import javax.annotation.Nonnull;

public final class World
{
  @Nonnull
  private final ComponentRegistry _componentRegistry;

  World( @Nonnull final ComponentManager<?>... components )
  {
    _componentRegistry = new ComponentRegistry( components );
  }

  @Nonnull
  Set<Class<?>> getComponentTypes()
  {
    return _componentRegistry.getComponentTypes();
  }

  @Nonnull
  public <T> ComponentAPI<T> getComponentByType( @Nonnull final Class<T> type )
  {
    return _componentRegistry.getComponentManagerByType( type ).getApi();
  }
}
