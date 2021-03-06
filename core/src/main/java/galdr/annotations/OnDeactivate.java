package galdr.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotation to identify method invoked after Subsystem completes operation.
 * This annotation will be processed on classes annotated by the {@link GaldrSubSystem} annotation and any parent
 * classes or interfaces.
 *
 * <p>If there are multiple methods annotated with this annotation then the methods declared in parent
 * classes will be invoked first and multiple methods within a single class will be invoked in declaration
 * order.</p>
 *
 * <p>The method that is annotated with this annotation must comply with the additional constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other galdr annotation</li>
 * <li>Must have 0 parameters</li>
 * <li>Must not return a value</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be abstract</li>
 * <li>Must not throw exceptions</li>
 * <li>Must be accessible to the class annotated by the {@link GaldrSubSystem} annotation.</li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
public @interface OnDeactivate
{
}
