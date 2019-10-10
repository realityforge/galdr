package galdr.annotations;

/**
 * Annotation that marks a component that needs to be processed by the Galdr annotation processor.
 *
 * <p>The type that is annotated with this annotation must comply with the additional constraints:</p>
 * <ul>
 * <li>Must be a class or an interface</li>
 * <li>Must not be a non-static nested class</li>
 * </ul>
 *
 * <p>The annotation processor that handles this annotation will analyze all super classes and super
 * interfaces. This includes analysis of default methods on interfaces.</p>
 */
public @interface Component
{
  /**
   * Define the strategy for storing the component instances.
   *
   * @return the strategy for storing the component instances.
   */
  Storage storage() default Storage.AUTODETECT;
}
