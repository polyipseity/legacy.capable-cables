package $group__.annotations;

import javax.annotation.Nonnull;
import javax.annotation.meta.TypeQualifierDefault;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Nonnull
@TypeQualifierDefault(METHOD)
@Retention(RUNTIME)
public @interface MethodsReturnNonnullByDefault { /* MARK empty */}
