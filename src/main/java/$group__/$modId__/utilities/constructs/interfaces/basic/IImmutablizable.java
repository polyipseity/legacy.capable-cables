package $group__.$modId__.utilities.constructs.interfaces.basic;

import $group__.$modId__.utilities.constructs.interfaces.annotations.ExternalToImmutableMethod;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import $group__.$modId__.utilities.helpers.Casts;
import $group__.$modId__.utilities.helpers.Reflections.Unsafe.AccessibleObjectAdapter.MethodAdapter;
import $group__.$modId__.utilities.helpers.Throwables;
import $group__.$modId__.utilities.variables.Globals;
import com.google.common.cache.CacheBuilder;

import javax.annotation.Nullable;
import javax.annotation.meta.When;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static $group__.$modId__.utilities.constructs.interfaces.annotations.ExternalToImmutableMethod.EXTERNAL_TO_IMMUTABLE_METHOD_ANNOTATIONS_CACHE;
import static $group__.$modId__.utilities.constructs.interfaces.annotations.ExternalToImmutableMethod.EXTERNAL_TO_IMMUTABLE_METHOD_MAP;
import static $group__.$modId__.utilities.helpers.Casts.castUnchecked;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxed;
import static $group__.$modId__.utilities.helpers.Optionals.unboxOptional;
import static $group__.$modId__.utilities.helpers.Reflections.isMemberStatic;
import static $group__.$modId__.utilities.helpers.Throwables.newThrowable;
import static $group__.$modId__.utilities.variables.Constants.GROUP;
import static $group__.$modId__.utilities.variables.Globals.LOGGER;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

@SuppressWarnings("SpellCheckingInspection")
public interface IImmutablizable<T> {
	/* SECTION methods */

	@OverridingStatus(group = GROUP, when = When.ALWAYS)
	T toImmutable();

	@OverridingStatus(group = GROUP, when = When.MAYBE)
	default boolean isImmutable() { return getClass().getSimpleName().toLowerCase(Locale.ROOT).contains("immutable"); }


	/* SECTION static variables */

	Set<Class<?>> BROKEN_TO_IMMUTABLE_CLASSES = Collections.newSetFromMap(CacheBuilder.newBuilder().softValues().<Class<?>, Boolean>build().asMap());


	/* SECTION static methods */

	static <T> Optional<T> tryToImmutable(@Nullable T o) {
		if (o == null) return Optional.empty();
		else if (o instanceof IImmutablizable<?>)
			return Casts.<IImmutablizable<T>>castUnchecked(o).map(IImmutablizable::toImmutable);

		Class<?> oc = o.getClass();
		if (ExternalToImmutableMethod.AnnotationProcessor.INSTANCE.isProcessed()) {
			if (BROKEN_TO_IMMUTABLE_CLASSES.contains(oc)) return Optional.of(o);

			MethodAdapter m;
			try {
				m = EXTERNAL_TO_IMMUTABLE_METHOD_MAP.get(EXTERNAL_TO_IMMUTABLE_METHOD_ANNOTATIONS_CACHE.get(oc));
			} catch (ExecutionException e) {
				LOGGER.warn("Unable to immutablize object '{}' of class '{}' as no to immutable method is found, will NOT attempt to immutable again, stacktrace:\n{}", o, oc.toGenericString(), getStackTrace(e));
				BROKEN_TO_IMMUTABLE_CLASSES.add(oc);
				return Optional.of(o);
			}

			Method m0 = m.get().orElseThrow(Throwables::unexpected);
			if (!m.setAccessible(true))
				LOGGER.warn("Unable to set to immutable method '{}' of object '{}' of class '{}' accessible, stacktrace:\n{}", m0.toGenericString(), o, oc.toGenericString(), getStackTrace(m.getCaughtThrowableUnboxedNonnull()));

			Optional<T> r = castUnchecked((isMemberStatic(m0) ? m.invoke(null, o) : m.invoke(o)).orElseGet(() -> {
				LOGGER.warn("To immutable method '{}' failed for object '{}' of class '{}', will NOT attempt to immutable again, stacktrace:\n{}", m0.toGenericString(), o, oc.toGenericString(), getStackTrace(m.getCaughtThrowableUnboxedNonnull()));
				BROKEN_TO_IMMUTABLE_CLASSES.add(oc);
				return castUncheckedUnboxed(o);
			}));

			if (!m.setAccessible(false))
				LOGGER.warn("Unable to restore accessibility of to immutable method '{}' of object '{}' of class '{}', presenting a potential security problem, stacktrace:\n{}", m0.toGenericString(), o, oc.toGenericString(), getStackTrace(m.getCaughtThrowableUnboxedNonnull()));

			return r;
		} else {
			LOGGER.debug("Unable to immutable object '{}' of class '{}' as to immutable method annotation is not yet processed, will attempt to immutable again, stacktrace:\n{}", o, oc.toGenericString(), getStackTrace(newThrowable()));
			return Optional.of(o);
		}
	}

	@Nullable
	static <T> T tryToImmutableUnboxed(@Nullable T o) { return unboxOptional(tryToImmutable(o)); }

	static <T> T tryToImmutableUnboxedNonnull(T o) { return tryToImmutable(o).orElseThrow(Globals::rethrowCaughtThrowableStatic); }
}
