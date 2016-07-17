package common;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Annotation for interface`s methods to mark it to behave as private-methods.
 *
 * Private methods in interfaces will be included in Java9, so, when public release will be done and project
 * become Java9-only, it will be strongly-recommended to replace this annotation with {@code private} modifier for all
 * marked methods.
 */
@Retention(SOURCE)
@Target(METHOD)
public @interface Private {
}
