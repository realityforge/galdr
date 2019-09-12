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
  private final Map<Class<? extends Component>, ComponentType> _componentTypeByClass;

  @SafeVarargs
  ComponentTypeRegistry( @Nonnull final Class<? extends Component>... types )
  {
    _componentTypes = new ComponentType[ types.length ];
    final Map<Class<? extends Component>, ComponentType> map = new HashMap<>();
    for ( int i = 0; i < types.length; i++ )
    {
      final Class<? extends Component> type = types[ i ];
      final ComponentType componentType = new ComponentType( type, i );
      map.put( type, componentType );
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
  ComponentType getComponentTypeByType( @Nonnull final Class<? extends Component> type )
  {
    return _componentTypeByClass.get( type );
  }
}
