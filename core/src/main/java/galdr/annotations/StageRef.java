package galdr.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

/**
 * Marks a template method that returns the named {@link galdr.ProcessorStage} instance.
 * This annotation can appear on classes annotated by {@link GaldrApplication}.
 *
 * <p>The method that is annotated with this annotation must also comply with the following constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other galdr annotation</li>
 * <li>Must not have any parameters</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be final</li>
 * <li>Must be abstract</li>
 * <li>Must not throw any exceptions</li>
 * <li>Must return an instance of {@link galdr.ProcessorStage}.</li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
public @interface StageRef
{
  /**
   * The name of the stage to return.
   * If not specified then the name defaults to the name of the method.
   *
   * @return the name of the stage to return.
   */
  @Nonnull
  String name() default "<default>";
}
