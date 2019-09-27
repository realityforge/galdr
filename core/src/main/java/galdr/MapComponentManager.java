package galdr;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import javax.annotation.Nonnull;

final class MapComponentManager<T>
  extends AllocatingComponentManager<T>
{
  //TODO: Replace this with a more efficient implementation that does not use boxed integers
  @Nonnull
  private final Map<Integer, T> _components = new HashMap<>();

  MapComponentManager( @Nonnull final World world,
                       final int id,
                       @Nonnull final Class<T> type,
                       @Nonnull final Supplier<T> createFn )
  {
    super( world, id, Flags.ALLOCATE, type, createFn );
  }

  @Nonnull
  @Override
  ComponentStorage getStorage()
  {
    return ComponentStorage.MAP;
  }

  @Nonnull
  @Override
  T performGet( final int entityId )
  {
    return Objects.requireNonNull( getComponents().get( entityId ) );
  }

  @Nonnull
  @Override
  T performCreate( final int entityId )
  {

    final T component = createComponentInstance();
    getComponents().put( entityId, component );
    return component;
  }

  @Override
  void performRemove( final int entityId )
  {
    getComponents().remove( entityId );
  }

  @Nonnull
  Map<Integer, T> getComponents()
  {
    return _components;
  }
}
