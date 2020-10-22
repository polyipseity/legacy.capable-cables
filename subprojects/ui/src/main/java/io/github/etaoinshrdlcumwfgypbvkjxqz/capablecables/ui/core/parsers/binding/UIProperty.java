package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.binding;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UIProperty {
	String[] value();
}
