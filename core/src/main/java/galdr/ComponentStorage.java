package galdr;

/**
 * The strategy for storing the components of a particular type for entities.
 */
public enum ComponentStorage
{
  /**
   * Components are never allocated and thus never stored.
   * This strategy is only expected to be used if the component class contains no state but a
   * component is to flag an entity. This strategy is typically used so that entities can be
   * flagged and efficiently processed by SubSystems.
   */
  NONE,
  /**
   * Components are stored in an array where the index into the array is the id of the entity.
   * This strategy has the fastest lookup time but is may result in higher memory usage. Slots
   * are allocated for the entity even if the entity never allocates a component of the associated
   * type. This strategy is ideally used when the component is allocated for a high proportion
   * of the components and the component is accessed with a high frequency.
   */
  ARRAY,
  /**
   * Components are stored in a map.
   * This strategy has a slower lookup time but components are only allocated when they are added to
   * an entity.
   */
  MAP
}
