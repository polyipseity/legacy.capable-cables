package $group__.$modId__.utilities.constructs.interfaces.extensions;

import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import $group__.$modId__.utilities.helpers.Reflections.Unsafe.AccessibleObjectAdapter.MethodAdapter;
import com.google.common.cache.CacheBuilder;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.meta.When;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;

import static $group__.$modId__.utilities.constructs.interfaces.annotations.ExternalCloneMethod.EXTERNAL_CLONE_METHOD_ANNOTATIONS_CACHE;
import static $group__.$modId__.utilities.constructs.interfaces.annotations.ExternalCloneMethod.EXTERNAL_CLONE_METHOD_MAP;
import static $group__.$modId__.utilities.helpers.Casts.castUnchecked;
import static $group__.$modId__.utilities.helpers.Masks.maskToNonnull;
import static $group__.$modId__.utilities.helpers.Reflections.isMemberStatic;
import static $group__.$modId__.utilities.variables.Constants.GROUP;
import static $group__.$modId__.utilities.variables.References.LOGGER;
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
	BiFunction<? super Throwable, ? super MethodAdapter, ? extends BiFunction<? super Object, ? super Object[], ?>> CALLBACK = (t, u) -> (v, w) -> {
		Class<?> vc = v.getClass();
		LOGGER.warn("Clone method '{}' failed for object '{}' of class '{}', will not attempt to clone again, stacktrace: {}", u.get().toGenericString(), v, vc.toGenericString(), getStackTrace(t));
		BROKEN_CLONE_CLASSES.add(vc);
		return v;
	};


	/* SECTION static methods */

	@Nullable
	static <T> T tryClone(@Nullable T o) {
		if (o == null) return null;
		else if (o instanceof ICloneable<?>) return castUnchecked(o, (ICloneable<T>) null).clone();

		Class<?> oc = o.getClass();
		if (BROKEN_CLONE_CLASSES.contains(oc)) return o;

		MethodAdapter m;
		try { m = EXTERNAL_CLONE_METHOD_MAP.get(EXTERNAL_CLONE_METHOD_ANNOTATIONS_CACHE.get(oc)); } catch (ExecutionException e) {
			LOGGER.warn("Unable to clone object '{}' of class '{}' as no clone method is found, will not attempt to clone again, stacktrace: {}", o, oc.toGenericString(), getStackTrace(e));
			BROKEN_CLONE_CLASSES.add(oc);
			return o;
		}

		m.setAccessible((t, u) -> v -> LOGGER.warn("Unable to set clone method '{}' of object '{}' of class '{}' accessible, stacktrace: {}", u.get().toGenericString(), o, oc.toGenericString(), getStackTrace(t)), true);

		return castUnchecked(isMemberStatic(m.get()) ? m.invokeNonnull(CALLBACK, null, o) : m.invokeNonnull(CALLBACK, o));
	}

	static <T> T tryCloneNonnull(T o) { return maskToNonnull(tryClone(o)); }
}
