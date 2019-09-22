package galdr;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import static org.realityforge.braincheck.Guards.*;

/**
 * Registry that maps ids and types to {@link ComponentManager} instances and vice versa.
 */
final class ComponentRegistry
{
  @Nonnull
  private final ComponentManager<?>[] _components;
  @Nonnull
  private final Map<Class<?>, ComponentManager<?>> _componentByClass;

  ComponentRegistry( @Nonnull final ComponentManager<?>... components )
  {
    _components = Galdr.shouldCopyArraysPassedToConstructors() ? new ComponentManager[ components.length ] : components;
    final Map<Class<?>, ComponentManager<?>> map = new HashMap<>();
    for ( int i = 0; i < components.length; i++ )
    {
      final ComponentManager component = components[ i ];
      if ( Galdr.shouldCheckApiInvariants() )
      {
        final int index = i;
        final int id = component.getId();
        invariant( () -> index == id,
                   () -> "Galdr-0003: Component named '" + component.getName() + "' has id " + id +
                         " but was passed as index " + index + "." );
      }
      map.put( component.getType(), component );
      if ( Galdr.shouldCopyArraysPassedToConstructors() )
      {
        _components[ i ] = component;
      }
    }
    _componentByClass = Collections.unmodifiableMap( map );
  }

  @Nonnull
  ComponentManager<?> getComponentManagerById( final int id )
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      invariant( () -> isComponentIdValid( id ),
                 () -> "Galdr-0002: ComponentRegistry.getComponentManagerByIndex() attempted to access Component " +
                       "with id " + id + " but no such component exists." );
    }
    return _components[ id ];
  }

  boolean isComponentIdValid( final int id )
  {
    return id >= 0 && id < _components.length;
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
