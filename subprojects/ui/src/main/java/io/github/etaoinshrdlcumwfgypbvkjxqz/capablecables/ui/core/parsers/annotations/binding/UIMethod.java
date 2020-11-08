package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.annotations.binding;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UIMethod {
	String[] value();
}
