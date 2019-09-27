package galdr;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The Component API exposed to application code.
 * A non-obvious aspect of the API is that the entityId parameter passed into the methods MUST
 * refer to an allocated and alive Entity instance.
 *
 * @param <T> the type of the component.
 */
public final class ComponentAPI<T>
{
  @Nonnull
  private final ComponentManager<T> _store;

  ComponentAPI( @Nonnull final ComponentManager<T> store )
  {
    _store = Objects.requireNonNull( store );
  }

  /**
   * Return the unique id of the component within the containing {@link World}.
   *
   * @return the unique id of the component within the containing {@link World}.
   */
  public int getId()
  {
    return _store.getId();
  }

  /**
   * Return the storage strategy used by the component.
   *
   * @return the storage strategy used by the component.
   */
  @Nonnull
  public ComponentStorage getStorage()
  {
    return _store.getStorage();
  }

  /**
   * Create the component instance for the specified entity.
   *
   * @param entityId the id of the entity.
   * @return the component instance.
   */
  @Nonnull
  public T create( final int entityId )
  {
    return _store.create( entityId );
  }

  /**
   * Allocate a component instance for the specified entity.
   * Unlike the {@link #create(int)} and {@link #findOrCreate(int)}, this method does not return the component.
   * It is expected that this method will be used for components that have no state.
   *
   * @param entityId the id of the entity.
   */
  public void allocate( final int entityId )
  {
     _store.allocate( entityId );
  }

  /**
   * Find the component instance or create a new one if it does not exist.
   *
   * @param entityId the id of the entity.
   * @return the component instance.
   */
  @Nonnull
  public T findOrCreate( final int entityId )
  {
    return _store.findOrCreate( entityId );
  }

  /**
   * Return true if the specified entity has a component contained in this store.
   * This method MUST NOT return <code>true</code> if the component is deleted or scheduled for deletion.
   *
   * @param entityId the id of the entity.
   * @return true if the specified entity has a component contained in this store, false otherwise.
   */
  public boolean has( final int entityId )
  {
    return _store.has( entityId );
  }

  /**
   * Return the component instance for the specified entity, if the component exists.
   *
   * @param entityId the id of the entity.
   * @return the component instance if it exists.
   */
  @Nullable
  public T find( final int entityId )
  {
    return _store.find( entityId );
  }

  /**
   * Return the component instance for the specified entity.
   * The component is expected to exist for the entity and it is an error to invoke
   * this method when the component does not exist.
   *
   * @param entityId the id of the entity.
   * @return the component instance.
   */
  @Nonnull
  public T get( final int entityId )
  {
    return _store.get( entityId );
  }

  /**
   * Remove the component for the specified entity.
   * The component MUST exist for the entity.
   *
   * @param entityId the id of the entity.
   */
  public void remove( final int entityId )
  {
    _store.remove( entityId );
  }
}
