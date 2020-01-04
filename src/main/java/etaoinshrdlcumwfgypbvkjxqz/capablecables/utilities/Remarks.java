package etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import java.lang.annotation.*;

/**
 *
 */
@Documented
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.CONSTRUCTOR, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE, ElementType.PACKAGE, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.SOURCE)
public @interface Remarks {
    String[] value();
}
