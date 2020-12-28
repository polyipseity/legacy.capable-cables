package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations;

import javax.annotation.meta.TypeQualifier;
import javax.annotation.meta.When;
import java.lang.annotation.*;

@Documented
@TypeQualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
public @interface Nonnull {
	When when() default When.ALWAYS;
}
