package galdr;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;

/**
 * Registry that maps the component types to indexes and vice versa.
 */
final class ComponentTypeRegistry
{
  @Nonnull
  private final ComponentManager<?>[] _componentTypes;
  @Nonnull
  private final Map<Class<?>, ComponentManager<?>> _componentTypeByClass;

  ComponentTypeRegistry( @Nonnull final ComponentManager<?>... componentTypes )
  {
    _componentTypes = new ComponentManager[ componentTypes.length ];
    final Map<Class<?>, ComponentManager<?>> map = new HashMap<>();
    for ( int i = 0; i < componentTypes.length; i++ )
    {
      final ComponentManager componentType = componentTypes[ i ];
      componentType.initIndex( i );
      map.put( componentType.getType(), componentType );
      _componentTypes[ i ] = componentType;
    }
    _componentTypeByClass = Collections.unmodifiableMap( map );
  }

  @Nonnull
  ComponentManager getComponentManagerByIndex( final int index )
  {
    return _componentTypes[ index ];
  }

  int size()
  {
    return _componentTypes.length;
  }

  @Nonnull
  ComponentManager getComponentManagerByType( @Nonnull final Class<?> type )
  {
    return _componentTypeByClass.get( type );
  }
}
