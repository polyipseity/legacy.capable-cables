package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Unmodifiable {}
