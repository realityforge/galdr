package galdr.spy;

import javax.annotation.Nonnull;

/**
 * A representation of a Subscription as exposed by the Spy subsystem.
 */
public interface SubscriptionInfo
{
  /**
   * Return the id of the subscription within the world.
   *
   * @return the id of the subscription within the world.
   */
  int getId();

  /**
   * Return the name of the Subscription.
   *
   * @return the name of the Subscription.
   */
  @Nonnull
  String getName();

  /**
   * Return true if the subscription has been disposed.
   * Other methods should not be invoked on a disposed subscription.
   *
   * @return true if the subscription has been disposed.
   */
  boolean isDisposed();

  /**
   * Return the entity collection containing all the entities of interest.
   *
   * @return the entity collection containing all the entities of interest.
   */
  @Nonnull
  CollectionInfo getCollection();
}
