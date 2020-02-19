package $group__.$modId__.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface InterfaceIntersection {
    /* SECTION static classes */;

    @Documented
    @Retention(RUNTIME)
    @Target(METHOD)
    @interface InstanceOfMethod { /* MARK empty */ }

    @Documented
    @Retention(RUNTIME)
    @Target(METHOD)
    @interface ConversionMethod { /* MARK empty */ }
}
