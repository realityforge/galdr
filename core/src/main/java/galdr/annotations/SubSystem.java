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
}
