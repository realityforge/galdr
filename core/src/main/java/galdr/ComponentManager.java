package galdr;

import java.util.Objects;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

/**
 * The class responsible for storing components of a particular type.
 */
abstract class ComponentManager<T>
{
  /**
   * The java type of the component.
   */
  @Nonnull
  private final Class<T> _type;
  /**
   * Function invoked to create an instance of the component.
   */
  @Nonnull
  private final Supplier<T> _createFn;
  /**
   * Unique index of type within a {@link World}.
   * Used to enable fast lookup and access of component data.
   */
  private int _index;

  ComponentManager( @Nonnull final Class<T> type, @Nonnull final Supplier<T> createFn )
  {
    _type = Objects.requireNonNull( type );
    _createFn = Objects.requireNonNull( createFn );
  }

  /**
   * Initialize the index. This occurs when the ComponentManager is placed in the registry.
   *
   * @param index the index of the component.
   */
  void initIndex( final int index )
  {
    assert 0 == _index;
    _index = index;
  }

  /**
   * Return the unique index of type within a {@link World}.
   *
   * @return the unique index of type within a {@link World}.
   */
  int getIndex()
  {
    return _index;
  }

  /**
   * Return the java type of the component.
   *
   * @return the java type of the component.
   */
  @Nonnull
  final Class<T> getType()
  {
    return _type;
  }

  /**
   * Return the function that creates an instance of the component.
   *
   * @return the function that creates an instance of the component.
   */
  @Nonnull
  final Supplier<T> getCreateFn()
  {
    return _createFn;
  }

  /**
   * Return the human readable name of the ComponentManager.
   * This method should NOT be invoked unless {@link Galdr#areNamesEnabled()} returns <code>true</code>.
   *
   * @return the human readable name of the ComponentManager.
   */
  @Nonnull
  String getName()
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      apiInvariant( Galdr::areNamesEnabled,
                    () -> "Galdr-0053: ComponentManager.getName() invoked when Galdr.areNamesEnabled() returns false" );
    }
    return _type.getSimpleName();
  }

  /**
   * Return true if the specified entity has a component contained in this store.
   * This method MUST NOT return <code>true</code> if the component is deleted or scheduled for deletion.
   *
   * @param entityId the id of the entity.
   * @return true if the specified entity has a component contained in this store, false otherwise.
   */
  final boolean has( final int entityId )
  {
    ensureEntityIdPositive( entityId );
    return performHas( entityId );
  }

  /**
   * Template method implemented by the ComponentManager implementation to check whether entity has component.
   *
   * @param entityId the id of the entity.
   */
  abstract boolean performHas( int entityId );

  /**
   * Return the component instance for the specified entity, if the component exists.
   *
   * @param entityId the id of the entity.
   * @return the component instance if it exists.
   */
  @Nullable
  final T find( final int entityId )
  {
    ensureEntityIdPositive( entityId );
    return performFind( entityId );
  }

  /**
   * Template method implemented by the ComponentManager implementation to find component for entity.
   *
   * @param entityId the id of the entity.
   */
  abstract T performFind( int entityId );

  /**
   * Return the component instance for the specified entity.
   * The component is expected to exist for the entity and it is an error to invoke
   * this method when the component does not exist.
   *
   * @param entityId the id of the entity.
   * @return the component instance.
   */
  @Nonnull
  final T get( final int entityId )
  {
    ensureEntityIdPositive( entityId );
    final T component = find( entityId );
    if ( Galdr.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> null != component,
                    () -> "Galdr-0033: The ComponentManager.get() method for the component named '" + getName() + "' " +
                          "expected to find a component for entityId " + entityId + " but is unable to " +
                          "locate component." );
    }
    assert null != component;
    return component;
  }

  /**
   * Find the component instance or create a new one if it does not exist.
   *
   * @param entityId the id of the entity.
   * @return the component instance.
   */
  @Nonnull
  final T findOrCreate( final int entityId )
  {
    final T component = find( entityId );
    return null == component ? create( entityId ) : component;
  }

  /**
   * Create the component instance for the specified entity.
   *
   * @param entityId the id of the entity.
   * @return the component instance.
   */
  @Nonnull
  final T create( final int entityId )
  {
    ensureEntityIdPositive( entityId );
    if ( Galdr.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> !has( entityId ),
                    () -> "Galdr-0031: The ComponentManager.create() method invoked but entity " + entityId +
                          " already has the component named '" + getName() + "'." );
    }
    return performCreate( entityId );
  }

  /**
   * Template method implemented by the ComponentManager implementation to perform component creation.
   *
   * @param entityId the id of the entity.
   */
  abstract T performCreate( int entityId );

  /**
   * Create an instance of the component.
   *
   * @return an instance of the component.
   */
  final T createComponentInstance()
  {
    return getCreateFn().get();
  }

  /**
   * Remove the component for the specified entity.
   * The component MUST exist for the entity.
   *
   * @param entityId the id of the entity.
   */
  final void remove( final int entityId )
  {
    ensureEntityIdPositive( entityId );
    if ( Galdr.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> has( entityId ),
                    () -> "Galdr-0030: The ComponentManager.remove() method for the component named '" + getName() +
                          "' was invoked but the entity " + entityId + " does not have the component." );
    }
    performRemove( entityId );
  }

  /**
   * Template method implemented by the ComponentManager implementation to perform removal.
   *
   * @param entityId the id of the entity.
   */
  abstract void performRemove( int entityId );

  @Override
  public String toString()
  {
    if ( Galdr.areDebugToStringMethodsEnabled() )
    {
      return "ComponentManager[" + getName() + "=" + _index + "]";
    }
    else
    {
      return super.toString();
    }
  }

  @Override
  public boolean equals( final Object o )
  {
    return o instanceof ComponentManager && _index == ( (ComponentManager) o )._index;
  }

  @Override
  public int hashCode()
  {
    return _index;
  }

  private void ensureEntityIdPositive( final int entityId )
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> entityId >= 0,
                    () -> "Galdr-0029: The ComponentManager method invoked was with a negative " +
                          "entityId " + entityId + "." );
    }
  }
}
