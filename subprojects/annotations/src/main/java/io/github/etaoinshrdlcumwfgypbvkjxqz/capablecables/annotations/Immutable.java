package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations;

import javax.annotation.meta.TypeQualifier;
import java.lang.annotation.*;

/**
 * Indicates that the annotated element is immutable.
 * Immutable means that the annotated element's exposed state cannot be modified here or elsewhere.
 *
 * @author William So
 * @since 0.0.1
 */
@Documented
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@TypeQualifier
public @interface Immutable {}
