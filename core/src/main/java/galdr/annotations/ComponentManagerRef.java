package galdr.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Marks a template method that returns a {@link galdr.ComponentManager} instance for a component type.
 * This annotation will be processed on classes annotated by the {@link SubSystem} annotation and any parent
 * classes or interfaces.
 *
 * <p>The method that is annotated with this annotation must also comply with the following constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other galdr annotation</li>
 * <li>Must not have any parameters</li>
 * <li>Must be abstract</li>
 * <li>Must not throw any exceptions</li>
 * <li>Must be accessible to the class annotated by the {@link SubSystem} annotation.</li>
 * <li>Must return an instance of {@link galdr.ComponentManager} that is parameterized with the type that defines the component.</li>
 * <li>
 *   Should not be public as not expected to be invoked outside the subsystem. A warning will be generated but can
 *   be suppressed by the {@link SuppressWarnings} or {@link SuppressGaldrWarnings} annotations with a key
 *   "Galdr:PublicRefMethod". This warning is also suppressed by the annotation processor if it is implementing
 *   an interface method.
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
public @interface ComponentManagerRef
{
}
