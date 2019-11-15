package galdr.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotation to identify method invoked before Subsystem begins operation.
 * This annotation will be processed on classes annotated by the {@link SubSystem} annotation and any parent
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
 * <li>Must be accessible to the class annotated by the {@link SubSystem} annotation.</li>
 * <li>
 *   Should not be public as not expected to be invoked except by the framework. A warning will be generated but can
 *   be suppressed by the {@link SuppressWarnings} or {@link SuppressGaldrWarnings} annotations with a key
 *   "Galdr:PublicRefMethod".
 * </li>
 * <li>
 *   Should not be protected if in the class annotated with the {@link SubSystem} annotation as the method is not
 *   expected to be invoked except by the framework. A warning will be generated but can be suppressed by the
 *   {@link SuppressWarnings} or {@link SuppressGaldrWarnings} annotations with a key "Galdr:ProtectedRefMethod".
 * </li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
public @interface OnActivate
{
}
