package galdr;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

/**
 * The class responsible for storing components of a particular type.
 */
abstract class ComponentStore<T>
{
  @Nonnull
  private final ComponentType<T> _componentType;

  ComponentStore( @Nonnull final ComponentType<T> componentType )
  {
    _componentType = Objects.requireNonNull( componentType );
  }

  @Nonnull
  final ComponentType<T> getComponentType()
  {
    return _componentType;
  }

  /**
   * Return the human readable name of the ComponentStore.
   * This method should NOT be invoked unless {@link Galdr#areNamesEnabled()} returns <code>true</code>.
   *
   * @return the human readable name of the ComponentStore.
   */
  @Nonnull
  final String getName()
  {
    return getComponentType().getName();
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
   * Template method implemented by the ComponentStore implementation to check whether entity has component.
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
   * Template method implemented by the ComponentStore implementation to find component for entity.
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
                    () -> "Galdr-0033: The ComponentStore.get() method for the component named '" + getName() + "' " +
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
                    () -> "Galdr-0031: The ComponentStore.create() method invoked but entity " + entityId +
                          " already has the component named '" + getName() + "'." );
    }
    return performCreate( entityId );
  }

  /**
   * Template method implemented by the ComponentStore implementation to perform component creation.
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
    return getComponentType().getCreateFn().get();
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
                    () -> "Galdr-0030: The ComponentStore.remove() method for the component named '" + getName() +
                          "' was invoked but the entity " + entityId + " does not have the component." );
    }
    performRemove( entityId );
  }

  /**
   * Template method implemented by the ComponentStore implementation to perform removal.
   *
   * @param entityId the id of the entity.
   */
  abstract void performRemove( int entityId );

  @Override
  public String toString()
  {
    if ( Galdr.areDebugToStringMethodsEnabled() )
    {
      return "ComponentStore[" + getName() + "]";
    }
    else
    {
      return super.toString();
    }
  }

  private void ensureEntityIdPositive( final int entityId )
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> entityId >= 0,
                    () -> "Galdr-0029: The ComponentStore method invoked was with a negative " +
                          "entityId " + entityId + "." );
    }
  }
}
