package $group__.$modId__.utilities.constructs.interfaces.extensions;

import $group__.$modId__.utilities.constructs.interfaces.annotations.ExternalCloneMethod;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import $group__.$modId__.utilities.helpers.Casts;
import $group__.$modId__.utilities.helpers.Loggers;
import $group__.$modId__.utilities.helpers.Reflections.Unsafe.AccessibleObjectAdapter.MethodAdapter;
import $group__.$modId__.utilities.helpers.Throwables;
import $group__.$modId__.utilities.variables.Globals;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.lang3.StringUtils;

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
import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.variables.Constants.GROUP;
import static $group__.$modId__.utilities.variables.Globals.LOGGER;
import static $group__.$modId__.utilities.variables.Globals.setCaughtThrowableStatic;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

public interface ICloneable<T> extends Cloneable {
	/* SECTION static variables */

	Set<Class<?>> BROKEN_CLONE_CLASSES = Collections.newSetFromMap(CacheBuilder.newBuilder().softValues().<Class<?>, Boolean>build().asMap());


	/* SECTION static methods */

	@Nullable
	static <T> T tryCloneUnboxed(@Nullable T o) { return unboxOptional(tryClone(o)); }

	@SuppressWarnings("ConstantConditions")
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
				setCaughtThrowableStatic(e);
				LOGGER.warn("Unable to clone object '{}' of class '{}'{}, will NOT attempt to clone again, stacktrace:\n{}", o, oc.toGenericString(), Thread.interrupted() ? " as no clone method is found" : StringUtils.EMPTY, getStackTrace(e));
				BROKEN_CLONE_CLASSES.add(oc);
				return Optional.of(o);
			}

			Method m0 = m.get().orElseThrow(Throwables::unexpected);
			boolean m0s = isMemberStatic(m0);
			if (!m.setAccessible(true))
				LOGGER.warn(Loggers.FORMATTER_WITH_THROWABLE.apply(Loggers.FORMATTER_REFLECTION_UNABLE_TO_SET_ACCESSIBLE.apply(() -> "clone method", m0).apply(m0s ? null : o, oc).apply(true), m.getCaughtThrowableUnboxedNonnull()));

			Optional<T> r = castUnchecked((m0s ? m.invoke(null, o) : m.invoke(o)).orElseGet(() -> {
				LOGGER.warn("Clone method '{}' failed for object '{}' of class '{}', will NOT attempt to clone again, stacktrace:\n{}", m0.toGenericString(), o, oc.toGenericString(), getStackTrace(m.getCaughtThrowableUnboxedNonnull()));
				BROKEN_CLONE_CLASSES.add(oc);
				return castUncheckedUnboxed(o);
			}));

			if (!m.setAccessible(false))
				LOGGER.warn(Loggers.FORMATTER_WITH_THROWABLE.apply(Loggers.FORMATTER_REFLECTION_UNABLE_TO_SET_ACCESSIBLE.apply(() -> "clone method", m0).apply(m0s ? null : o, oc).apply(false), m.getCaughtThrowableUnboxedNonnull()));

			return r;
		} else {
			LOGGER.debug("Unable to clone object '{}' of class '{}' as clone method annotation is NOT yet processed, will attempt to clone again, stacktrace:\n{}", o, oc.toGenericString(), getStackTrace(newThrowable()));
			return Optional.of(o);
		}
	}

	static <T> T tryCloneUnboxedNonnull(T o) { return tryClone(o).orElseThrow(Globals::rethrowCaughtThrowableStatic); }


	/* SECTION methods */

	@OverridingStatus(group = GROUP, when = When.NEVER)
	default T copy() { return clone(); }

	@OverridingMethodsMustInvokeSuper
	@OverridingStatus(group = GROUP, when = When.ALWAYS)
	default T clone() { throw rejectUnsupportedOperation(); }
}
