package $group__.$modId__.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({})
@Retention(RUNTIME)
public @interface Property {
	/* SECTION methods */

	String key() default "";

	String value() default "";
}
