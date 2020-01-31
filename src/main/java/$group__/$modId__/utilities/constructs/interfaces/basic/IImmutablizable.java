package $group__.$modId__.utilities.constructs.interfaces.basic;

import $group__.$modId__.utilities.constructs.interfaces.annotations.ExternalToImmutableMethod;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import $group__.$modId__.utilities.helpers.Casts;
import $group__.$modId__.utilities.helpers.Loggers;
import $group__.$modId__.utilities.helpers.Reflections.Unsafe.AccessibleObjectAdapter.MethodAdapter;
import $group__.$modId__.utilities.helpers.Throwables;
import $group__.$modId__.utilities.variables.Globals;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.MapMaker;

import javax.annotation.Nullable;
import javax.annotation.meta.When;
import java.lang.ref.SoftReference;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static $group__.$modId__.utilities.helpers.Casts.castUnchecked;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxed;
import static $group__.$modId__.utilities.helpers.MapsExtension.MULTI_THREAD_WEAK_KEY_MAP_MAKER;
import static $group__.$modId__.utilities.helpers.Optionals.unboxOptional;
import static $group__.$modId__.utilities.helpers.Reflections.getSuperclassesAndInterfaces;
import static $group__.$modId__.utilities.helpers.Reflections.isMemberStatic;
import static $group__.$modId__.utilities.helpers.Throwables.newThrowable;
import static $group__.$modId__.utilities.helpers.Throwables.throw_;
import static $group__.$modId__.utilities.variables.Constants.GROUP;
import static $group__.$modId__.utilities.variables.Constants.MULTI_THREAD_THREAD_COUNT;
import static $group__.$modId__.utilities.variables.Globals.LOGGER;
import static $group__.$modId__.utilities.variables.Globals.setCaughtThrowableStatic;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

/**
 * Indicates this object can be turned into an immutable version of itself.
 *
 * @author William So
 * @param <T> type of the immutable version of this object
 * @see ExternalToImmutableMethod for the to-immutable system
 * @since 0.0.1.0
 */
@SuppressWarnings("SpellCheckingInspection")
@FunctionalInterface
public interface IImmutablizable<T> {
	/* SECTION static variables */

	ConcurrentMap<ExternalToImmutableMethod, MethodAdapter> EXTERNAL_METHOD_MAP = MULTI_THREAD_WEAK_KEY_MAP_MAKER.makeMap();
	LoadingCache<Class<?>, ExternalToImmutableMethod> EXTERNAL_ANNOTATIONS_CACHE = CacheBuilder.newBuilder().concurrencyLevel(MULTI_THREAD_THREAD_COUNT).build(new CacheLoader<Class<?>, ExternalToImmutableMethod>() {
		@SuppressWarnings("ConstantConditions")
		@Override
		public ExternalToImmutableMethod load(Class<?> key) throws NoSuchMethodException {
			@Nullable ExternalToImmutableMethod r = null;
			List<Map.Entry<Class<?>, ExternalToImmutableMethod>> l = EXTERNAL_ANNOTATIONS_CACHE.asMap().entrySet().stream().filter(t -> t.getValue().allowExtends() && t.getKey().isAssignableFrom(key)).collect(Collectors.toList());

			sss:
			for (LinkedHashSet<Class<?>> ss : getSuperclassesAndInterfaces(key))
				for (Map.Entry<Class<?>, ExternalToImmutableMethod> e : l)
					if (ss.contains(e.getKey())) {
						r = e.getValue();
						break sss;
					}

			if (r != null)
				LOGGER.debug("To immutable method '{}' with annotation '{}' auto-registered for class '{}'", EXTERNAL_METHOD_MAP.get(r).get().orElseThrow(Throwables::unexpected).toGenericString(), r, key.toGenericString());
			else
				throw throw_(new NoSuchMethodException("No to-immutable method for class '" + key.toGenericString() + "'"));

			return r;
		}
	});

	/**
	 * A {@link Set} of classes that cannot be turned into an immutable version of
	 * itself.
	 *
	 * @implNote using {@link SoftReference} for values to make this into a cache
	 *
	 * @see #tryToImmutable
	 * @since 0.0.1.0
	 */
	Set<Class<?>> BROKEN_CLASS_SET = Collections.newSetFromMap(new MapMaker().weakKeys().concurrencyLevel(MULTI_THREAD_THREAD_COUNT).makeMap());


	/* SECTION static variables */

	/**
	 * Attempts to turn the given parameter {@code o} into an immutable version of it.
	 * <p>
	 * The given object (hence {@code o}) will attempt to turn into an immutable
	 * version of itself through, in order:
	 * <ol>
	 *     <li>If {@code o} is {@code null}, return {@code null}.</li>
	 *     <li>If {@code o} is {@code instanceof} this interface, return the invocation
	 *     result of {@link #toImmutable} on {@code o}.</li>
	 *     <li>If {@link ExternalToImmutableMethod} is not
	 *     {@linkplain IAnnotationProcessor#isProcessed processed}, return
	 *     {@code o}.</li>
	 *     <li>Otherwise, if {@link #BROKEN_CLASS_SET}
	 *     {@link Set#contains contains} the {@link Class} of {@code o}, return
	 *     {@code o}.</li>
	 *     <li>Attempts to get the {@linkplain MethodAdapter to-immutable method} from
	 *     {@link #EXTERNAL_METHOD_MAP}, and if
	 *     a {@link Throwable} is caught, put the {@code Class} of {@code o} into
	 *     {@code BROKEN_TO_IMMUTABLE_CLASSES} and {@code return} {@code o}.</li>
	 *     <li>Else, the {@code MethodAdapter} is invoked and if a
	 *     {@code Throwable} is caught, put the {@code Class} of {@code o} into
	 *     {@code BROKEN_TO_IMMUTABLE_CLASSES} and {@code return} {@code o}.</li>
	 *     <li>Lastly, {@code return} the result of the invoked {@code MethodAdapter}.</li>
	 * </ol>
	 * The result is always wrapped in an {@link Optional} before {@code return}ing.
	 *
	 * @param o object that will attempt to turn into an immutable version of itself
	 * @param <T> type of parameter {@code o}
	 * @return
	 * an immutable version of parameter {@code o} if it exists, else parameter
	 * {@code o}, wrapped in an {@link Optional}
	 * @see ExternalToImmutableMethod for the to-immutable system
	 * @see #tryToImmutableUnboxed unboxed version
	 * @see #tryToImmutableUnboxedNonnull unboxed nonnull version
	 * @since 0.0.1.0
	 */
	@SuppressWarnings("ConstantConditions")
	static <T> Optional<T> tryToImmutable(@Nullable T o) {
		if (o == null) return Optional.empty();
		else if (o instanceof IImmutablizable<?>)
			return Casts.<IImmutablizable<T>>castUnchecked(o).map(IImmutablizable::toImmutable);

		Class<?> oc = o.getClass();
		if (ExternalToImmutableMethod.AnnotationProcessor.INSTANCE.isProcessed()) {
			if (BROKEN_CLASS_SET.contains(oc)) return Optional.of(o);

			MethodAdapter m;
			try {
				m = EXTERNAL_METHOD_MAP.get(EXTERNAL_ANNOTATIONS_CACHE.get(oc));
			} catch (ExecutionException e) {
				setCaughtThrowableStatic(e);
				LOGGER.warn("Unable to immutablize object '{}' of class '{}' as no to immutable method is obtained, will NOT attempt to immutable again, stacktrace:\n{}", o, oc.toGenericString(), getStackTrace(e));
				BROKEN_CLASS_SET.add(oc);
				return Optional.of(o);
			}

			Method m0 = m.get().orElseThrow(Throwables::unexpected);
			boolean m0s = isMemberStatic(m0);
			if (!m.setAccessible(true))
				LOGGER.warn(Loggers.FORMATTER_WITH_THROWABLE.apply(Loggers.FORMATTER_REFLECTION_UNABLE_TO_SET_ACCESSIBLE.apply(() -> "to-immutable method", m0).apply(m0s ? null : o, oc).apply(true), m.getCaughtThrowableUnboxedNonnull()));

			Optional<T> r = castUnchecked((m0s ? m.invoke(null, o) : m.invoke(o)).orElseGet(() -> {
				LOGGER.warn("To-immutable method '{}' failed for object '{}' of class '{}', will NOT attempt to immutable again, stacktrace:\n{}", m0.toGenericString(), o, oc.toGenericString(), getStackTrace(m.getCaughtThrowableUnboxedNonnull()));
				BROKEN_CLASS_SET.add(oc);
				return castUncheckedUnboxed(o);
			}));

			if (!m.setAccessible(false))
				LOGGER.warn(Loggers.FORMATTER_WITH_THROWABLE.apply(Loggers.FORMATTER_REFLECTION_UNABLE_TO_SET_ACCESSIBLE.apply(() -> "to-immutable method", m0).apply(m0s ? null : o, oc).apply(false), m.getCaughtThrowableUnboxedNonnull()));

			return r;
		} else {
			LOGGER.debug("Unable to immutable object '{}' of class '{}' as to immutable method annotation is NOT yet processed, will attempt to immutable again, stacktrace:\n{}", o, oc.toGenericString(), getStackTrace(newThrowable()));
			return Optional.of(o);
		}
	}

	/**
	 * See {@link #tryToImmutable}.
	 * <p>
	 * Useful when you want to get the unwrapped result object.
	 *
	 * @param o object that will attempt to turn into an immutable version of itself
	 * @param <T> type of parameter {@code o}
	 * @return
	 * an immutable version of parameter {@code o} if it exists, else parameter
	 * {@code o}, wrapped in an {@link Optional}
	 * @see #tryToImmutable overloaded version
	 * @see #tryToImmutableUnboxedNonnull unboxed nonnull version
	 * @since 0.0.1.0
	 */
	@Nullable
	static <T> T tryToImmutableUnboxed(@Nullable T o) { return unboxOptional(tryToImmutable(o)); }

	/**
	 * See {@link #tryToImmutable}.
	 * <p>
	 * Useful when you want to get the unwrapped result object, and know the returned
	 * object is nonnull as the parameter {@code o} is nonnull.
	 *
	 * @param o object that will attempt to turn into an immutable version of itself
	 * @param <T> type of parameter {@code o}
	 * @return
	 * an immutable version of parameter {@code o} if it exists, else parameter
	 * {@code o}, nullable
	 * @see #tryToImmutable overloaded version
	 * @see #tryToImmutableUnboxed unboxed version
	 * @since 0.0.1.0
	 */
	static <T> T tryToImmutableUnboxedNonnull(T o) { return tryToImmutable(o).orElseThrow(Globals::rethrowCaughtThrowableStatic); }


	/* SECTION methods */

	/**
	 * Returns an immutable version of this object.
	 * <p>
	 * The returned object may be {@code this} if {@link #isImmutable} returns
	 * {@code true}.
	 *
	 * @return the immutable version of this object
	 * @see #isImmutable
	 * @since 0.0.1.0
	 */
	@OverridingStatus(group = GROUP, when = When.ALWAYS)
	T toImmutable();

	/**
	 * Returns whether this object is immutable.
	 * <p>
	 * {@link #toImmutable} may return {@code this} if this method returns
	 * {@code true}.
	 *
	 * @return whether this object is immutable
	 * @see #toImmutable
	 * @since 0.0.1.0
	 */
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	default boolean isImmutable() { return getClass().getSimpleName().toLowerCase(Locale.ROOT).contains("immutable"); }
}
