package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations;

import javax.annotation.Nullable;
import javax.annotation.meta.TypeQualifierDefault;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Nullable
@TypeQualifierDefault(LOCAL_VARIABLE)
@Retention(RUNTIME)
public @interface LocalVariablesAreNullableByDefault {}
