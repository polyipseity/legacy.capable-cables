package $group__.$modId__.utilities.constructs.interfaces.annotations;

import javax.annotation.Nonnull;
import javax.annotation.meta.TypeQualifierDefault;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 *
 */
@Documented
@Nonnull
@TypeQualifierDefault(FIELD)
@Retention(RUNTIME)
public @interface FieldsAreNonnullByDefault { /* MARK empty */}
