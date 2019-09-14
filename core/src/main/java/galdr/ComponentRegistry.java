package galdr;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import static org.realityforge.braincheck.Guards.*;

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
      final ComponentManager component = components[ i ];
      if ( Galdr.shouldCheckApiInvariants() )
      {
        final int index = i;
        final int suppliedIndex = component.getIndex();
        invariant( () -> index == suppliedIndex,
                   () -> "Galdr-0003: Component named '" + component.getName() + "' has index " + suppliedIndex +
                         " but was passed as index " + index + "." );
      }
      map.put( component.getType(), component );
      _components[ i ] = component;
    }
    _componentByClass = Collections.unmodifiableMap( map );
  }

  @Nonnull
  ComponentManager<?> getComponentManagerByIndex( final int index )
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      invariant( () -> index >= 0 && index < _components.length,
                 () -> "Galdr-0002: ComponentRegistry.getComponentManagerByIndex() attempted to access Component " +
                       "at index " + index + " but no such component exists." );
    }
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
    final ComponentManager<T> componentManager = (ComponentManager<T>) _componentByClass.get( type );
    if ( Galdr.shouldCheckApiInvariants() )
    {
      invariant( () -> null != componentManager,
                 () -> "Galdr-0001: ComponentRegistry.getComponentManagerByType() attempted to access Component " +
                       "for type " + type + " but no such component exists." );
    }
    return componentManager;
  }

  @Nonnull
  Set<Class<?>> getComponentTypes()
  {
    return _componentByClass.keySet();
  }
}
