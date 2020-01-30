package $group__.$modId__.utilities.constructs.interfaces.annotations;

import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({})
@Retention(RUNTIME)
public @interface Property {
	/* SECTION methods */

	String key() default StringUtils.EMPTY;

	String value() default StringUtils.EMPTY;
}
