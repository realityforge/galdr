package galdr.spy;

import javax.annotation.Nonnull;

/**
 * A representation of the world as exposed by the Spy subsystem.
 */
public interface WorldInfo
{
  /**
   * Return the name of the world.
   *
   * @return the name of the world.
   */
  @Nonnull
  String getName();

  /**
   * Return the number of entities that currently exist in the world.
   *
   * @return the number of entities that currently exist in the world.
   */
  int getEntityCount();

  /**
   * Return the number of entities that the world can contain without allocating more storage.
   *
   * @return the number of entities that the world can contain without allocating more storage.
   */
  int getEntityCapacity();
}
