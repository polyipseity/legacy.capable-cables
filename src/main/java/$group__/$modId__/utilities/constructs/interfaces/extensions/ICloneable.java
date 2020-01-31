package $group__.$modId__.utilities.constructs.interfaces.extensions;

import $group__.$modId__.utilities.constructs.interfaces.annotations.ExternalCloneMethod;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import $group__.$modId__.utilities.constructs.interfaces.basic.IAnnotationProcessor;
import $group__.$modId__.utilities.helpers.Casts;
import $group__.$modId__.utilities.helpers.Loggers;
import $group__.$modId__.utilities.helpers.Reflections.Unsafe.AccessibleObjectAdapter.MethodAdapter;
import $group__.$modId__.utilities.helpers.Throwables;
import $group__.$modId__.utilities.variables.Globals;
import com.google.common.annotations.Beta;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalNotification;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.meta.When;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static $group__.$modId__.utilities.helpers.Casts.castUnchecked;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxed;
import static $group__.$modId__.utilities.helpers.MapsExtension.MULTI_THREAD_WEAK_KEY_MAP_MAKER;
import static $group__.$modId__.utilities.helpers.Optionals.unboxOptional;
import static $group__.$modId__.utilities.helpers.Reflections.Unsafe.getDeclaredMethod;
import static $group__.$modId__.utilities.helpers.Reflections.getSuperclassesAndInterfaces;
import static $group__.$modId__.utilities.helpers.Reflections.isMemberStatic;
import static $group__.$modId__.utilities.helpers.Throwables.newThrowable;
import static $group__.$modId__.utilities.helpers.Throwables.throw_;
import static $group__.$modId__.utilities.variables.Constants.GROUP;
import static $group__.$modId__.utilities.variables.Constants.MULTI_THREAD_THREAD_COUNT;
import static $group__.$modId__.utilities.variables.Globals.*;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

public interface ICloneable<T> extends Cloneable {
	/* SECTION static variables */

	MethodAdapter DEFAULT_METHOD = MethodAdapter.of(getDeclaredMethod(Object.class, "clone").get().orElseGet(() -> {
		LOGGER.warn("Default clone method access failed", getCaughtThrowableUnboxedStatic());
		return null;
	}));
	@Nullable
	ExternalCloneMethod DEFAULT_ANNOTATION = DEFAULT_METHOD.get().isPresent() ? new ExternalCloneMethod() {
		/* SECTION methods */

		@Override
		public Class<?>[] value() { return new Class<?>[0]; }

		@Override
		public boolean allowExtends() { return false; }


		@Override
		public Class<ExternalCloneMethod> annotationType() { return ExternalCloneMethod.class; }
	} : null;
	AtomicLong EVICTION_COUNT = new AtomicLong();

	ConcurrentMap<ExternalCloneMethod, MethodAdapter> EXTERNAL_METHOD_MAP = MULTI_THREAD_WEAK_KEY_MAP_MAKER.makeMap();
	LoadingCache<Class<?>, ExternalCloneMethod> EXTERNAL_ANNOTATIONS_CACHE = CacheBuilder.newBuilder().maximumWeight(10000L).weigher((k, v) -> v == DEFAULT_ANNOTATION ? 10000 / (int) Math.min(10000L, 100L + EVICTION_COUNT.get()) : 0).removalListener((RemovalNotification<Class<?>, ExternalCloneMethod> t) -> LOGGER.debug("Default clone method '{}' cached for class '{}' evicted, count: {}", DEFAULT_METHOD.get().orElseThrow(Throwables::unexpected), t.getKey().toGenericString(), EVICTION_COUNT.incrementAndGet())).concurrencyLevel(MULTI_THREAD_THREAD_COUNT).build(new CacheLoader<Class<?>, ExternalCloneMethod>() {
		/* SECTION methods */

		@SuppressWarnings("ConstantConditions")
		@Override
		public ExternalCloneMethod load(Class<?> key) throws NoSuchMethodException {
			@Nullable ExternalCloneMethod r = null;
			List<Map.Entry<Class<?>, ExternalCloneMethod>> l = EXTERNAL_ANNOTATIONS_CACHE.asMap().entrySet().stream().filter(t -> t.getValue().allowExtends() && t.getKey().isAssignableFrom(key)).collect(Collectors.toList());

			sss:
			for (LinkedHashSet<Class<?>> ss : getSuperclassesAndInterfaces(key))
				for (Map.Entry<Class<?>, ExternalCloneMethod> e : l)
					if (ss.contains(e.getKey())) {
						r = e.getValue();
						break sss;
					}

			if (r != null)
				LOGGER.debug("Clone method '{}' with annotation '{}' auto-registered for class '{}'", EXTERNAL_METHOD_MAP.get(r).get().orElseThrow(Throwables::unexpected).toGenericString(), r, key.toGenericString());
			else if ((r = DEFAULT_ANNOTATION) == null)
				throw throw_(new NoSuchMethodException("Default clone method inaccessible for class '" + key.toGenericString() + "'"));
			else
				LOGGER.debug("Default clone method '{}' cached for class '{}'", DEFAULT_METHOD.get().orElseThrow(Throwables::unexpected).toGenericString(), key.toGenericString());

			return r;
		}
	});

	Set<Class<?>> BROKEN_CLASS_SET = Collections.newSetFromMap(MULTI_THREAD_WEAK_KEY_MAP_MAKER.makeMap());


	/* SECTION static methods */

	/**
	 * Attempts to clone the given parameter {@code o}.
	 * <p>
	 * The given object (hence {@code o}) will attempt to clone itself through, in
	 * order:
	 * <ol>
	 *     <li>If {@code o} is {@code null}, return {@code null}.</li>
	 *     <li>If {@code o} is {@code instanceof} this interface, return the invocation
	 *     result of {@link #clone} on {@code o}.</li>
	 *     <li>If {@link ExternalCloneMethod} is not
	 *     {@linkplain IAnnotationProcessor#isProcessed() processed}, return
	 *     {@code o}.</li>
	 *     <li>Otherwise, if {@link #BROKEN_CLASS_SET}
	 *     {@link Set#contains contains} the {@link Class} of {@code o}, return
	 *     {@code o}.</li>
	 *     <li>Attempts to get the {@linkplain MethodAdapter clone method} from
	 *     {@link #EXTERNAL_METHOD_MAP}, and if
	 *     a {@link Throwable} is caught, put the {@code Class} of {@code o} into
	 *     {@code BROKEN_CLONE_CLASSES} and {@code return} {@code o}.</li>
	 *     <li>Else, the {@code MethodAdapter} is invoked and if a
	 *     {@code Throwable} is caught, put the {@code Class} of {@code o} into
	 *     {@code BROKEN_CLONE_CLASSES} and {@code return} {@code o}.</li>
	 *     <li>Lastly, {@code return} the result of the invoked {@code MethodAdapter}.</li>
	 * </ol>
	 * The result is always wrapped in an {@link Optional} before {@code return}ing.
	 *
	 * @param o object that will attempt to clone itself
	 * @param <T> type of parameter {@code o}
	 * @return
	 * a clone of parameter {@code o} if it successful, else parameter
	 * {@code o}, wrapped in an {@link Optional}
	 * @see ExternalCloneMethod for the cloning system
	 * @see #tryCloneUnboxed unboxed version
	 * @see #tryCloneUnboxedNonnull unboxed nonnull version
	 * @since 0.0.1.0
	 */
	@SuppressWarnings("ConstantConditions")
	static <T> Optional<T> tryClone(@Nullable T o) {
		if (o == null) return Optional.empty();
		else if (o instanceof ICloneable<?>) return Casts.<ICloneable<T>>castUnchecked(o).map(ICloneable::copy);

		Class<?> oc = o.getClass();
		if (ExternalCloneMethod.AnnotationProcessor.INSTANCE.isProcessed()) {
			if (BROKEN_CLASS_SET.contains(oc)) return Optional.of(o);

			MethodAdapter m;
			try {
				m = EXTERNAL_METHOD_MAP.get(EXTERNAL_ANNOTATIONS_CACHE.get(oc));
			} catch (ExecutionException e) {
				setCaughtThrowableStatic(e);
				LOGGER.warn("Unable to clone object '{}' of class '{}' as no clone method is obtained, will NOT attempt to clone again, stacktrace:\n{}", o, oc.toGenericString(), getStackTrace(e));
				BROKEN_CLASS_SET.add(oc);
				return Optional.of(o);
			}

			Method m0 = m.get().orElseThrow(Throwables::unexpected);
			boolean m0s = isMemberStatic(m0);
			if (!m.setAccessible(true))
				LOGGER.warn(Loggers.FORMATTER_WITH_THROWABLE.apply(Loggers.FORMATTER_REFLECTION_UNABLE_TO_SET_ACCESSIBLE.apply(() -> "clone method", m0).apply(m0s ? null : o, oc).apply(true), m.getCaughtThrowableUnboxedNonnull()));

			Optional<T> r = castUnchecked((m0s ? m.invoke(null, o) : m.invoke(o)).orElseGet(() -> {
				LOGGER.warn("Clone method '{}' failed for object '{}' of class '{}', will NOT attempt to clone again, stacktrace:\n{}", m0.toGenericString(), o, oc.toGenericString(), getStackTrace(m.getCaughtThrowableUnboxedNonnull()));
				BROKEN_CLASS_SET.add(oc);
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

	/**
	 * See {@link #tryClone}.
	 * <p>
	 * Useful when you want to get the unwrapped result object.
	 *
	 * @param o object that will attempt to clone itself
	 * @param <T> type of parameter {@code o}
	 * @return
	 * a clone of parameter {@code o} if it successful, else parameter
	 * {@code o}, nullable
	 * @see #tryClone overloaded version
	 * @see #tryCloneUnboxedNonnull unboxed nonnull version
	 * @since 0.0.1.0
	 */
	@Nullable
	static <T> T tryCloneUnboxed(@Nullable T o) { return unboxOptional(tryClone(o)); }

	/**
	 * See {@link #tryClone}.
	 * <p>
	 * Useful when you want to get the unwrapped result object, and know the returned
	 * object is nonnull as the parameter {@code o} is nonnull.
	 *
	 * @param o object that will attempt to clone itself
	 * @param <T> type of parameter {@code o}
	 * @return
	 * a clone of parameter {@code o} if it successful, else parameter
	 * {@code o}
	 * @see #tryClone overloaded version
	 * @see #tryCloneUnboxed unboxed version
	 * @since 0.0.1.0
	 */
	static <T> T tryCloneUnboxedNonnull(T o) { return tryClone(o).orElseThrow(Globals::rethrowCaughtThrowableStatic); }


	/* SECTION methods */

	/**
	 * Alias for {@link #clone}.
	 * <p>
	 * Prefer this method over {@code clone}, as invoking {@code clone} directly may
	 * incur {@link NoSuchMethodException}, and {@link ClassNotFoundException} if used
	 * as a method reference.
	 *
	 * @implSpec consider this method as {@code final}
	 *
	 * @return cloned object of this object
	 * @see #clone aliased version
	 * @since 0.0.1.0
	 */
	@OverridingStatus(group = GROUP, when = When.NEVER)
	default T copy() { return clone(); }

	/**
	 * {@inheritDoc}
	 * <p>
	 * Prefer {@link #copy} over this method, as invoking {@code clone} directly may
	 * incur {@link NoSuchMethodException}, and {@link ClassNotFoundException} if used
	 * as a method reference.
	 *
	 * @implSpec must override this method even though {@code Object} overrides it as
	 * {@link OverridingStatus.AnnotationProcessor} will check it
	 *
	 * @return cloned object of this object
	 * @see #copy safe version
	 * @since 0.0.1.0
	 */
	@Beta
	@OverridingMethodsMustInvokeSuper
	@OverridingStatus(group = GROUP, when = When.ALWAYS)
	T clone();
}
