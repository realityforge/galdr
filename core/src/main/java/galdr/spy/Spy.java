package galdr.spy;

import javax.annotation.Nonnull;

/**
 * Interface for interacting with spy subsystem.
 */
public interface Spy
{
  /**
   * Add a spy handler to the list of handlers.
   * The handler should not already be in the list.
   *
   * @param handler the spy handler.
   */
  void addSpyEventHandler( @Nonnull SpyEventHandler handler );

  /**
   * Remove spy handler from list of existing handlers.
   * The handler should already be in the list.
   *
   * @param handler the spy handler.
   */
  void removeSpyEventHandler( @Nonnull SpyEventHandler handler );

  /**
   * Return true if spy events will be propagated.
   * This means spies are enabled and there is at least one spy event handler present.
   *
   * @return true if spy events will be propagated, false otherwise.
   */
  boolean willPropagateSpyEvents();

  /**
   * Report an event in the Galdr system.
   *
   * @param event the event that occurred.
   */
  void reportSpyEvent( @Nonnull Object event );

  /**
   * Return the info object representing the world.
   *
   * @return the info object representing the world.
   */
  @Nonnull
  WorldInfo asWorldInfo();

  /**
   * Return the component that models the specified type.
   *
   * @param componentType the type of the component.
   * @return the component info.
   */
  @Nonnull
  ComponentInfo getComponentByType( @Nonnull Class<?> componentType );
}
