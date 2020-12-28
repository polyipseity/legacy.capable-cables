package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations;

import javax.annotation.meta.TypeQualifierDefault;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Indicates that the fields of the scope of the annotated element are nullable by default.
 *
 * @author William So
 * @since Capable Cables 0.0.1
 */
@Documented
@Nullable
@TypeQualifierDefault(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldsAreNullableByDefault {}
