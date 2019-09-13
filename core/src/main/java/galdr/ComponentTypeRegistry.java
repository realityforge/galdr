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
  private final ComponentType[] _componentTypes;
  @Nonnull
  private final Map<Class<?>, ComponentType> _componentTypeByClass;

  ComponentTypeRegistry( @Nonnull final ComponentType... componentTypes )
  {
    _componentTypes = new ComponentType[ componentTypes.length ];
    final Map<Class<?>, ComponentType> map = new HashMap<>();
    for ( int i = 0; i < componentTypes.length; i++ )
    {
      final ComponentType componentType = componentTypes[ i ];
      componentType.initIndex( i );
      map.put( componentType.getType(), componentType );
      _componentTypes[ i ] = componentType;
    }
    _componentTypeByClass = Collections.unmodifiableMap( map );
  }

  @Nonnull
  ComponentType getComponentTypeByIndex( final int index )
  {
    return _componentTypes[ index ];
  }

  int size()
  {
    return _componentTypes.length;
  }

  @Nonnull
  ComponentType getComponentTypeByType( @Nonnull final Class<?> type )
  {
    return _componentTypeByClass.get( type );
  }
}
