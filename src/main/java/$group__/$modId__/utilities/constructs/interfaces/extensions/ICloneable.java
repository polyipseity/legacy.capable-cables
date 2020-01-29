package $group__.$modId__.utilities.constructs.interfaces.extensions;

import $group__.$modId__.utilities.constructs.interfaces.annotations.ExternalCloneMethod;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import $group__.$modId__.utilities.helpers.Casts;
import $group__.$modId__.utilities.helpers.Reflections.Unsafe.AccessibleObjectAdapter.MethodAdapter;
import $group__.$modId__.utilities.helpers.Throwables;
import $group__.$modId__.utilities.variables.Globals;
import com.google.common.cache.CacheBuilder;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.meta.When;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static $group__.$modId__.utilities.constructs.interfaces.annotations.ExternalCloneMethod.EXTERNAL_CLONE_METHOD_ANNOTATIONS_CACHE;
import static $group__.$modId__.utilities.constructs.interfaces.annotations.ExternalCloneMethod.EXTERNAL_CLONE_METHOD_MAP;
import static $group__.$modId__.utilities.helpers.Casts.castUnchecked;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxed;
import static $group__.$modId__.utilities.helpers.Optionals.unboxOptional;
import static $group__.$modId__.utilities.helpers.Reflections.isMemberStatic;
import static $group__.$modId__.utilities.helpers.Throwables.newThrowable;
import static $group__.$modId__.utilities.variables.Constants.GROUP;
import static $group__.$modId__.utilities.variables.Globals.LOGGER;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

public interface ICloneable<T> extends Cloneable {
	/* SECTION methods */

	/** {@inheritDoc} */
	@OverridingMethodsMustInvokeSuper
	@OverridingStatus(group = GROUP, when = When.ALWAYS)
	T clone();


	@OverridingStatus(group = GROUP, when = When.NEVER)
	default T copy() { return clone(); }


	/* SECTION static variables */
	Set<Class<?>> BROKEN_CLONE_CLASSES = Collections.newSetFromMap(CacheBuilder.newBuilder().softValues().<Class<?>, Boolean>build().asMap());


	/* SECTION static methods */

	static <T> Optional<T> tryClone(@Nullable T o) {
		if (o == null) return Optional.empty();
		else if (o instanceof ICloneable<?>) return Casts.<ICloneable<T>>castUnchecked(o).map(ICloneable::copy);

		Class<?> oc = o.getClass();
		if (ExternalCloneMethod.AnnotationProcessor.INSTANCE.isProcessed()) {
			if (BROKEN_CLONE_CLASSES.contains(oc)) return Optional.of(o);

			MethodAdapter m;
			try {
				m = EXTERNAL_CLONE_METHOD_MAP.get(EXTERNAL_CLONE_METHOD_ANNOTATIONS_CACHE.get(oc));
			} catch (ExecutionException e) {
				LOGGER.warn("Unable to clone object '{}' of class '{}' as no clone method is found, will NOT attempt to clone again, stacktrace:\n{}", o, oc.toGenericString(), getStackTrace(e));
				BROKEN_CLONE_CLASSES.add(oc);
				return Optional.of(o);
			}

			Method m0 = m.get().orElseThrow(Throwables::unexpected);
			if (!m.setAccessible(true))
				LOGGER.warn("Unable to set clone method '{}' of object '{}' of class '{}' accessible, stacktrace:\n{}", m0.toGenericString(), o, oc.toGenericString(), getStackTrace(m.getCaughtThrowableUnboxedNonnull()));

			Optional<T> r = castUnchecked((isMemberStatic(m0) ? m.invoke(null, o) : m.invoke(o)).orElseGet(() -> {
				LOGGER.warn("Clone method '{}' failed for object '{}' of class '{}', will NOT attempt to clone again, stacktrace:\n{}", m0.toGenericString(), o, oc.toGenericString(), getStackTrace(m.getCaughtThrowableUnboxedNonnull()));
				BROKEN_CLONE_CLASSES.add(oc);
				return castUncheckedUnboxed(o);
			}));

			if (!m.setAccessible(false))
				LOGGER.warn("Unable to restore accessibility of clone method '{}' of object '{}' of class '{}', presenting a potential security problem, stacktrace:\n{}", m0.toGenericString(), o, oc.toGenericString(), getStackTrace(m.getCaughtThrowableUnboxedNonnull()));

			return r;
		} else {
			LOGGER.debug("Unable to clone object '{}' of class '{}' as clone method annotation is NOT yet processed, will attempt to clone again, stacktrace:\n{}", o, oc.toGenericString(), getStackTrace(newThrowable()));
			return Optional.of(o);
		}
	}

	@Nullable
	static <T> T tryCloneUnboxed(@Nullable T o) { return unboxOptional(tryClone(o)); }

	static <T> T tryCloneUnboxedNonnull(T o) { return tryClone(o).orElseThrow(Globals::rethrowCaughtThrowableStatic); }
}
