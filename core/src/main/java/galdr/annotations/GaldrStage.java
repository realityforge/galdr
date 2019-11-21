package galdr.annotations;

import galdr.Stage;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

/**
 * Annotation that defines a stage and all the subsystems that are part of the stage.
 * This annotation will be processed on classes annotated by {@link GaldrApplication}.
 *
 * <p>The method that is annotated with this annotation must also comply with the following constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other galdr annotation</li>
 * <li>Must not have any parameters</li>
 * <li>Must be abstract</li>
 * <li>Must not throw any exceptions</li>
 * <li>Must return an instance of {@link Stage}.</li>
 * <li>Must be accessible to the class annotated by the {@link GaldrApplication} annotation.</li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
public @interface GaldrStage
{
  /**
   * The name of the stage to return.
   * If not specified then the name defaults to the name of the method.
   *
   * @return the name of the stage to return.
   */
  @Nonnull
  String name() default "<default>";

  /**
   * Return the set of subsystems that the stage is composed from.
   *
   * @return the set of subsystems that the stage is composed from.
   */
  Class<?>[] value();
}
