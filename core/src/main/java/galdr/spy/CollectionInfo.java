package galdr.spy;

import galdr.AreaOfInterest;
import javax.annotation.Nonnull;

/**
 * A representation of an EntityCollection as exposed by the Spy subsystem.
 * An EntityCollection is a set of entities maintained by the runtime. The entities match an
 * {@link AreaOfInterest}. One or more subscribers are subscribed to the collection. At any one
 * time, a single subscriber may be iterating over the collection.
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
