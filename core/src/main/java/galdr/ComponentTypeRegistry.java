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
  private final ComponentStore<?>[] _componentTypes;
  @Nonnull
  private final Map<Class<?>, ComponentStore<?>> _componentTypeByClass;

  ComponentTypeRegistry( @Nonnull final ComponentStore<?>... componentTypes )
  {
    _componentTypes = new ComponentStore[ componentTypes.length ];
    final Map<Class<?>, ComponentStore<?>> map = new HashMap<>();
    for ( int i = 0; i < componentTypes.length; i++ )
    {
      final ComponentStore componentType = componentTypes[ i ];
      componentType.initIndex( i );
      map.put( componentType.getType(), componentType );
      _componentTypes[ i ] = componentType;
    }
    _componentTypeByClass = Collections.unmodifiableMap( map );
  }

  @Nonnull
  ComponentStore getComponentStoreByIndex( final int index )
  {
    return _componentTypes[ index ];
  }

  int size()
  {
    return _componentTypes.length;
  }

  @Nonnull
  ComponentStore getComponentStoreByType( @Nonnull final Class<?> type )
  {
    return _componentTypeByClass.get( type );
  }
}
