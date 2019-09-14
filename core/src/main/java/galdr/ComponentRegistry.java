package galdr;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;

/**
 * Registry that maps the {@link ComponentManager} to indexes and vice versa.
 */
final class ComponentRegistry
{
  @Nonnull
  private final ComponentManager<?>[] _components;
  @Nonnull
  private final Map<Class<?>, ComponentManager<?>> _componentByClass;

  ComponentRegistry( @Nonnull final ComponentManager<?>... components )
  {
    _components = new ComponentManager[ components.length ];
    final Map<Class<?>, ComponentManager<?>> map = new HashMap<>();
    for ( int i = 0; i < components.length; i++ )
    {
      final ComponentManager componentType = components[ i ];
      componentType.initIndex( i );
      map.put( componentType.getType(), componentType );
      _components[ i ] = componentType;
    }
    _componentByClass = Collections.unmodifiableMap( map );
  }

  @Nonnull
  ComponentManager<?> getComponentManagerByIndex( final int index )
  {
    return _components[ index ];
  }

  int size()
  {
    return _components.length;
  }

  @SuppressWarnings( "unchecked" )
  @Nonnull
  <T> ComponentManager<T> getComponentManagerByType( @Nonnull final Class<T> type )
  {
    return (ComponentManager<T>) _componentByClass.get( type );
  }
}
