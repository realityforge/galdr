package galdr.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

/**
 * Annotation to identify method invoked to process entities.
 *
 * <p>The method that is annotated with this must comply with the additional constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other galdr annotation</li>
 * <li>Must have 1 integer parameters</li>
 * <li>Must not return a value</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be abstract</li>
 * <li>Must not throw exceptions</li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
public @interface EntityProcessor
{
  /**
   * Classes of components that an entity MUST have to be matched.
   *
   * @return the classes of components that an entity MUST have to be matched.
   */
  @Nonnull
  Class<?>[] all() default {};

  /**
   * Classes of components that an entity MUST have at least one of to be matched.
   *
   * @return the classes of components that an entity MUST have at least one of to be matched.
   */
  @Nonnull
  Class<?>[] one() default {};

  /**
   * Classes of components that an entity MUST NOT have to be matched.
   *
   * @return the classes of components that an entity MUST NOT have to be matched.
   */
  @Nonnull
  Class<?>[] exclude() default {};
}
