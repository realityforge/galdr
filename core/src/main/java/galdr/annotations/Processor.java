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
 * <li>Must be accessible to the class annotated by the {@link GaldrSubSystem} annotation.</li>
 * <li>
 *   Should not be public as not expected to be invoked outside the subsystem. A warning will be generated but can
 *   be suppressed by the {@link SuppressWarnings} or {@link SuppressGaldrWarnings} annotations with a key
 *   "Galdr:PublicLifecycleMethod". This warning is also suppressed by the annotation processor if it is implementing
 *   an interface method.
 * </li>
 * <li>
 *   Should not be protected if in the class annotated with the {@link GaldrSubSystem} annotation as the method is not
 *   expected to be invoked outside the subsystem. A warning will be generated but can be suppressed by the
 *   {@link SuppressWarnings} or {@link SuppressGaldrWarnings} annotations with a key "Galdr:ProtectedLifecycleMethod".
 * </li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
public @interface Processor
{
}
