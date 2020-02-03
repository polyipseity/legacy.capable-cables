package $group__.$modId__.utilities.constructs.interfaces.annotations;

import $group__.$modId__.utilities.helpers.StringsExtension;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({})
@Retention(RUNTIME)
public @interface Property {
	/* SECTION methods */

	String key() default StringsExtension.EMPTY;

	String value() default StringsExtension.EMPTY;
}
