package galdr.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotation used to mark a class that is processed by the Galdr annotation processor and is used to
 * instantiate the world.
 *
 * <p>The type that is annotated with this annotation must comply with the additional constraints:</p>
 * <ul>
 * <li>Must be a class</li>
 * <li>Must be abstract</li>
 * <li>Must not be final</li>
 * <li>Must not be a non-static nested class</li>
 * <li>Must have at least one method annotated with {@link GaldrStage} or {@link WorldRef}</li>
 * </ul>
 */
@Documented
@Target( ElementType.TYPE )
public @interface GaldrApplication
{
}
