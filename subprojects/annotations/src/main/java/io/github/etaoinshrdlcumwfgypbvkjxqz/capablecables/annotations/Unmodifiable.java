package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations;

import java.lang.annotation.*;

/**
 * Indicates that the annotated element is unmodifiable.
 * Unmodifiable means that the annotated element's exposed state cannot be modified here.  However, the exposed state
 * could be modified elsewhere.
 * <p>
 * {@link Immutable} provides a stronger guarantee of immutability.
 *
 * @author William So
 * @see Immutable
 * @since 0.0.1
 */
@Documented
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Unmodifiable {}
