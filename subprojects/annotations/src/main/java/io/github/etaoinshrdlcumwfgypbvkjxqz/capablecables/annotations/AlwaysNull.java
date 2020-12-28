package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations;

import javax.annotation.meta.TypeQualifierNickname;
import javax.annotation.meta.When;
import java.lang.annotation.*;

@Documented
@TypeQualifierNickname
@Nonnull(when = When.NEVER)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
public @interface AlwaysNull {}
