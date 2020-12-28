package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations;

import java.lang.annotation.*;

/**
 * Indicates that the annotated element is ordered.
 * The meaning of 'ordered' is up to the context.
 *
 * @author William So
 * @since Capable Cables 0.0.1
 */
@Documented
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Ordered {}
