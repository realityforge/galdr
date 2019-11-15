package galdr.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotate the method that will be overridden to provide the "name" of the subsystem.
 * This annotation will be processed on classes annotated by the {@link SubSystem} annotation and any parent
 * classes or interfaces.
 *
 * <p>The method that is annotated with the annotation must comply with the additional constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other galdr annotation</li>
 * <li>Must have 0 parameters</li>
 * <li>Must return a String</li>
 * <li>Must be abstract</li>
 * <li>Must not throw exceptions</li>
 * <li>Must be accessible to the class annotated by the {@link SubSystem} annotation.</li>
 * <li>
 *   Should not be public as not expected to be invoked outside the subsystem. A warning will be generated but can
 *   be suppressed by the {@link SuppressWarnings} or {@link SuppressGaldrWarnings} annotations with a key
 *   "Galdr:PublicRefMethod".
 * </li>
 * <li>
 *   Should not be protected if in the class annotated with the {@link SubSystem} annotation as the method is not
 *   expected to be invoked outside the subsystem. A warning will be generated but can be suppressed by the
 *   {@link SuppressWarnings} or {@link SuppressGaldrWarnings} annotations with a key "Galdr:ProtectedRefMethod".
 * </li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
public @interface NameRef
{
}
