package $group__.$modId__.annotations;

import javax.annotation.meta.When;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

@Documented
@Target(METHOD)
@Retention(SOURCE)
public @interface OverridingStatus {
	/* SECTION methods */

	String group();

	When when() default When.UNKNOWN;
}
