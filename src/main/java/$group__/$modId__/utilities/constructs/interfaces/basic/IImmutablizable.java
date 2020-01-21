package $group__.$modId__.utilities.constructs.interfaces.basic;

import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import $group__.$modId__.utilities.helpers.Reflections.Unsafe.AccessibleObjectAdapter.MethodAdapter;
import com.google.common.cache.CacheBuilder;

import javax.annotation.Nullable;
import javax.annotation.meta.When;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;

import static $group__.$modId__.utilities.constructs.interfaces.annotations.ExternalToImmutableMethod.EXTERNAL_TO_IMMUTABLE_METHOD_ANNOTATIONS_CACHE;
import static $group__.$modId__.utilities.constructs.interfaces.annotations.ExternalToImmutableMethod.EXTERNAL_TO_IMMUTABLE_METHOD_MAP;
import static $group__.$modId__.utilities.helpers.Casts.castChecked;
import static $group__.$modId__.utilities.helpers.Casts.castUnchecked;
import static $group__.$modId__.utilities.helpers.Masks.maskToNonnull;
import static $group__.$modId__.utilities.helpers.Reflections.isMemberStatic;
import static $group__.$modId__.utilities.variables.Constants.GROUP;
import static $group__.$modId__.utilities.variables.References.LOGGER;
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
	BiFunction<? super Throwable, ? super MethodAdapter, ? extends BiFunction<? super Object, ? super Object[], ?>> CALLBACK = (t, u) -> (v, w) -> {
		Class<?> vc = v.getClass();
		LOGGER.warn("To immutable method '{}' failed for object '{}' of class '{}', will not attempt to immutable again, stacktrace: {}", u.get().toGenericString(), v, vc.toGenericString(), getStackTrace(t));
		BROKEN_TO_IMMUTABLE_CLASSES.add(vc);
		return v;
	};


	/* SECTION static methods */

	@Nullable
	static <T> T tryToImmutable(@Nullable T o) {
		if (o == null) return null;
		else if (o instanceof IImmutablizable<?>) return castUnchecked(o, (IImmutablizable<T>) null).toImmutable();

		Class<?> oc = o.getClass();
		if (BROKEN_TO_IMMUTABLE_CLASSES.contains(oc)) return o;

		MethodAdapter m;
		try { m = EXTERNAL_TO_IMMUTABLE_METHOD_MAP.get(EXTERNAL_TO_IMMUTABLE_METHOD_ANNOTATIONS_CACHE.get(oc)); } catch (ExecutionException e) {
			LOGGER.warn("Unable to immutablize object '{}' of class '{}' as no to immutable method is found, will not attempt to immutable again, stacktrace: {}", o, oc.toGenericString(), getStackTrace(e));
			BROKEN_TO_IMMUTABLE_CLASSES.add(oc);
			return o;
		}

		m.setAccessible((t, u) -> v -> LOGGER.warn("Unable to set to immutable method '{}' of object '{}' of class '{}' accessible, stacktrace: {}", u.get().toGenericString(), o, oc.toGenericString(), getStackTrace(t)), true);

		return castChecked(isMemberStatic(m.get()) ? m.invokeNonnull(CALLBACK, null, o) : m.invokeNonnull(CALLBACK, o));
	}

	static <T> T tryToImmutableNonnull(T o) { return maskToNonnull(tryToImmutable(o)); }
}
