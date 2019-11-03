package galdr.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotation to identify method invoked to perform logic for subsystem.
 * The method can have zero or one parameters. If there is one parameter then the it is the delta parameter passed to
 * the {@link galdr.ProcessorStage#process(int)} method that results in this method being invoked.
 *
 * <p>The method that is annotated with this must comply with the additional constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other galdr annotation</li>
 * <li>Must have either 0 or 1 integer parameters.</li>
 * <li>Must not return a value</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be abstract</li>
 * <li>Must not throw exceptions</li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
public @interface Processor
{
}
