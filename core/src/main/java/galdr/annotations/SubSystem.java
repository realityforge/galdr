package galdr.annotations;

import javax.annotation.Nonnull;

/**
 * Annotation that marks a system that needs to be processed by the Galdr annotation processor.
 *
 * <p>The type that is annotated with this annotation must comply with the additional constraints:</p>
 * <ul>
 * <li>Must be a class or an interface</li>
 * <li>Must be abstract</li>
 * <li>Must not be final</li>
 * <li>Must not be a non-static nested class</li>
 * </ul>
 *
 * <p>The annotation processor that handles this annotation will analyze all super classes and super
 * interfaces. This includes analysis of default methods on interfaces.</p>
 */
public @interface SubSystem
{
  /**
   * Return the name of the system.
   * The value must conform to the requirements of a java identifier.
   *
   * @return the name of the system.
   */
  @Nonnull
  String name() default "<default>";

  /**
   * Return the name of the stage that the processor is added to.
   *
   * @return the name of the stage that the processor is added to.
   */
  @Nonnull
  String stage() default "<default>";

  /**
   * Return the priority of the processor.
   * Processors with lower priority will execute earlier in a stage.
   * Processors with the same priority will be ordered based on a dependency analysis or if the processors
   * are independent then then by the declaration order.
   *
   * @return the priority of the processor.
   */
  int priority() default Priority.DEFAULT;

  /**
   * The class containing priority constants.
   */
  final class Priority
  {
    /**
     * The default priority if no other priority is specified.
     */
    public static final int DEFAULT = 1000;

    private Priority()
    {
    }
  }
}
