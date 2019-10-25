package galdr.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Marks a template method that returns a {@link galdr.ComponentAPI} instance for a component type.
 * This annotation will be processed on classes annotated by either {@link GaldrApplication} or {@link SubSystem}.
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
 * <li>Must return an instance of {@link galdr.ComponentAPI}.</li>
 * <li>The return type MUST be parameterized with the type that defines the component.</li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
public @interface ComponentRef
{
}
