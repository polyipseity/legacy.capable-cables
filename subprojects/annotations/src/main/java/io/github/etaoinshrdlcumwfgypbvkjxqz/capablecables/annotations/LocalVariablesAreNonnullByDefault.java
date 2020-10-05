package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations;

import javax.annotation.Nonnull;
import javax.annotation.meta.TypeQualifierDefault;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Indicates that the local variables of the scope of the annotated element are nonnull by default.
 *
 * @author William So
 * @since 0.0.1
 */
@Documented
@Nonnull
@TypeQualifierDefault(ElementType.LOCAL_VARIABLE)
@Retention(RetentionPolicy.RUNTIME)
public @interface LocalVariablesAreNonnullByDefault {}
