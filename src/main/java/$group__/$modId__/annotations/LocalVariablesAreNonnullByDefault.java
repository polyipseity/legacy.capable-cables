package $group__.$modId__.annotations;

import javax.annotation.Nonnull;
import javax.annotation.meta.TypeQualifierDefault;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Nonnull
@TypeQualifierDefault(LOCAL_VARIABLE)
@Retention(RUNTIME)
public @interface LocalVariablesAreNonnullByDefault { /* MARK empty */ }
