package galdr;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The Component API exposed to application code.
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
