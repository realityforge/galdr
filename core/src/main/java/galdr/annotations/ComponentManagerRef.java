package galdr.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Marks a template method that returns a {@link galdr.ComponentManager} instance for a component type.
 * This annotation will be processed on classes annotated by {@link SubSystem}.
 *
 * <p>The method that is annotated with this annotation must also comply with the following constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other galdr annotation</li>
 * <li>Must not have any parameters</li>
 * <li>Must be abstract</li>
 * <li>Must not throw any exceptions</li>
 * <li>Must return an instance of {@link galdr.ComponentManager} that is parameterized with the type that defines the component.</li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
public @interface ComponentManagerRef
{
}
