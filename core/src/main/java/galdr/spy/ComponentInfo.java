package galdr.spy;

import javax.annotation.Nonnull;

/**
 * A representation of the Component as exposed by the Spy subsystem.
 */
public interface ComponentInfo
{
  /**
   * Return the id of the component.
   * The Id is unique within the World.
   *
   * @return the id of the component.
   */
  int getId();

  /**
   * Return the name of the component.
   *
   * @return the name of the component.
   */
  @Nonnull
  String getName();

  /**
   * Return the number of collections where this component is part of the AreaOfInterest.
   *
   * @return the number of collections where this component is part of the AreaOfInterest.
   */
  int getCollectionCount();
}
