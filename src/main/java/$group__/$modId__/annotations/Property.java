package $group__.$modId__.annotations;

import $group__.$modId__.utilities.helpers.specific.StringsExtension;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({})
@Retention(RUNTIME)
public @interface Property {
	/* SECTION methods */

	String key() default StringsExtension.STRING_EMPTY;

	String value() default StringsExtension.STRING_EMPTY;
}
