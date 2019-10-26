package galdr.spy;

import galdr.AreaOfInterest;
import javax.annotation.Nonnull;

/**
 * A representation of an EntityCollection as exposed by the Spy subsystem.
 */
public interface CollectionInfo
{
  /**
   * Return true if there are no subscribers associated with the collection.
   * Other methods should not be invoked on a disposed collection.
   *
   * @return true if there are no subscribers associated with the collection.
   */
  boolean isDisposed();

  /**
   * Return the AreaOfInterest that describes the entities that should be contained in the collection.
   *
   * @return the AreaOfInterest that describes the entities that should be contained in the collection.
   */
  @Nonnull
  AreaOfInterest getAreaOfInterest();

  /**
   * Return the number of subscriptions that are connected to this collection.
   *
   * @return the number of subscriptions that are connected to this collection.
   */
  int getSubscriptionCount();

  /**
   * Return the number of entities that are contained in the subscription.
   *
   * @return the number of entities that are contained in the subscription.
   */
  int getEntityCount();
}
