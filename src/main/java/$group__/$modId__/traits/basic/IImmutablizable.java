package $group__.$modId__.traits.basic;

import $group__.$modId__.annotations.ExternalToImmutableMethod;
import $group__.$modId__.annotations.OverridingStatus;
import $group__.$modId__.utilities.helpers.Casts;
import $group__.$modId__.utilities.helpers.specific.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.meta.When;
import java.lang.invoke.MethodHandle;
import java.lang.ref.SoftReference;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static $group__.$modId__.utilities.helpers.Dynamics.getSuperclassesAndInterfaces;
import static $group__.$modId__.utilities.helpers.specific.Loggers.EnumMessages.FACTORY_PARAMETERIZED_MESSAGE;
import static $group__.$modId__.utilities.helpers.specific.Loggers.EnumMessages.SUFFIX_WITH_THROWABLE;
import static $group__.$modId__.utilities.helpers.specific.MapsExtension.*;
import static $group__.$modId__.utilities.helpers.specific.Optionals.unboxOptional;
import static $group__.$modId__.utilities.helpers.specific.Throwables.*;
import static $group__.$modId__.utilities.variables.Constants.*;
import static $group__.$modId__.utilities.variables.Globals.LOGGER;
import static java.util.Collections.newSetFromMap;

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

	ConcurrentMap<ExternalToImmutableMethod, MethodHandle> EXTERNAL_METHOD_MAP = MAP_MAKER_MULTI_THREAD_WEAK_KEYS.makeMap();
	ConcurrentMap<Class<?>, ExternalToImmutableMethod> EXTERNAL_ANNOTATIONS_MAP = MAP_MAKER_MULTI_THREAD.makeMap();
	LoadingCache<Class<?>, ExternalToImmutableMethod> EXTERNAL_ANNOTATIONS_CACHE = CacheBuilder.newBuilder().initialCapacity(INITIAL_SIZE_LARGE).concurrencyLevel(MULTI_THREAD_THREAD_COUNT).expireAfterAccess(CACHE_EXPIRATION_ACCESS_DURATION, CACHE_EXPIRATION_ACCESS_TIME_UNIT).build(new CacheLoader<Class<?>, ExternalToImmutableMethod>() {
		/* SECTION methods */

		@Override
		public ExternalToImmutableMethod load(Class<?> key) throws NoSuchMethodException {
			@Nullable ExternalToImmutableMethod r = EXTERNAL_ANNOTATIONS_MAP.get(key);
			if (r != null) return r;

			List<Map.Entry<Class<?>, ExternalToImmutableMethod>> l = EXTERNAL_ANNOTATIONS_CACHE.asMap().entrySet().stream().filter(t -> t.getValue().allowExtends() && t.getKey().isAssignableFrom(key)).collect(Collectors.toList());
			sss:
			for (LinkedHashSet<Class<?>> ss : getSuperclassesAndInterfaces(key))
				for (Map.Entry<Class<?>, ExternalToImmutableMethod> e : l)
					if (ss.contains(e.getKey())) {
						r = e.getValue();
						break sss;
					}

			if (r != null) {
				ExternalToImmutableMethod rf = r;
				LOGGER.debug(() -> FACTORY_PARAMETERIZED_MESSAGE.makeMessage("To immutable method '{}' with annotation '{}' auto-registered for class '{}'", EXTERNAL_METHOD_MAP.get(rf), rf, key.toGenericString()));
			}
			else
				throw throw_(new NoSuchMethodException("No to-immutable method for class '" + key.toGenericString() + '\''));

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
	Set<Class<?>> BROKEN_CLASS_SET = newSetFromMap(MAP_MAKER_MULTI_THREAD_WEAK_KEYS.makeMap());


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
	 *     <li>Attempts to get the {@linkplain MethodHandle to-immutable method} from
	 *     {@link #EXTERNAL_METHOD_MAP}, and if
	 *     a {@link Throwable} is caught, put the {@code Class} of {@code o} into
	 *     {@code BROKEN_TO_IMMUTABLE_CLASSES} and {@code return} {@code o}.</li>
	 *     <li>Else, the {@code MethodHandle} is invoked and if a
	 *     {@code Throwable} is caught, put the {@code Class} of {@code o} into
	 *     {@code BROKEN_TO_IMMUTABLE_CLASSES} and {@code return} {@code o}.</li>
	 *     <li>Lastly, {@code return} the result of the invoked {@code MethodHandle}.</li>
	 * </ol>
	 * The result is always wrapped in an {@link Optional} before {@code return}ing.
	 *
	 * @param <T> type of parameter {@code o}
	 * @param o object that will attempt to turn into an immutable version of itself
	 * @param logger {@link Logger} used for logging
	 * @return
	 * an immutable version of parameter {@code o} if it exists, else parameter
	 * {@code o}, wrapped in an {@link Optional}
	 * @see ExternalToImmutableMethod for the to-immutable system
	 * @see #tryToImmutableUnboxed unboxed version
	 * @see #tryToImmutableUnboxedNonnull unboxed nonnull version
	 * @since 0.0.1.0
	 */
	@SuppressWarnings("ConstantConditions")
	static <T> Optional<T> tryToImmutable(@Nullable T o, Logger logger) {
		if (o == null) return Optional.empty();
		else if (o instanceof IImmutablizable<?>)
			return Casts.<IImmutablizable<T>>castUnchecked(o).map(IImmutablizable::toImmutable);

		Class<?> oc = o.getClass();
		if (ExternalToImmutableMethod.AnnotationProcessor.INSTANCE.isProcessed()) {
			if (BROKEN_CLASS_SET.contains(oc)) return Optional.of(o);

			MethodHandle m;
			try {
				m = EXTERNAL_METHOD_MAP.get(EXTERNAL_ANNOTATIONS_CACHE.get(oc));
			} catch (ExecutionException e) {
				setCaughtThrowableStatic(e, logger);
				logger.warn(() -> SUFFIX_WITH_THROWABLE.makeMessage(FACTORY_PARAMETERIZED_MESSAGE.makeMessage("Unable to immutablize object '{}' of class '{}' as no to immutable method is obtained, will NOT attempt to immutable again", o, oc.toGenericString()), e));
				BROKEN_CLASS_SET.add(oc);
				return Optional.of(o);
			}

			Optional<T> r = tryCall(() -> m.invoke(o), logger).flatMap(Casts::castUnchecked);
			consumeIfCaughtThrowable(t -> {
				logger.warn(() -> SUFFIX_WITH_THROWABLE.makeMessage(FACTORY_PARAMETERIZED_MESSAGE.makeMessage("To-immutable method '{}' failed for object '{}' of class '{}', will NOT attempt to immutable again", m, o, oc.toGenericString()), t));
				BROKEN_CLASS_SET.add(oc);
			});
			return r;
		} else {
			logger.debug(() -> SUFFIX_WITH_THROWABLE.makeMessage(FACTORY_PARAMETERIZED_MESSAGE.makeMessage("Unable to immutable object '{}' of class '{}' as to immutable method annotation is NOT yet processed, will attempt to immutable again", o, oc.toGenericString()), newThrowable()));
			return Optional.of(o);
		}
	}

	/**
	 * See {@link #tryToImmutable}.
	 * <p>
	 * Useful when you want to get the unwrapped result object.
	 *
	 * @param <T> type of parameter {@code o}
	 * @param o object that will attempt to turn into an immutable version of itself
	 * @param logger {@link Logger} used for logging
	 * @return
	 * an immutable version of parameter {@code o} if it exists, else parameter
	 * {@code o}, wrapped in an {@link Optional}
	 * @see #tryToImmutable overloaded version
	 * @see #tryToImmutableUnboxedNonnull unboxed nonnull version
	 * @since 0.0.1.0
	 */
	@Nullable
	static <T> T tryToImmutableUnboxed(@Nullable T o, Logger logger) { return unboxOptional(tryToImmutable(o, logger)); }

	/**
	 * See {@link #tryToImmutable}.
	 * <p>
	 * Useful when you want to get the unwrapped result object, and know the returned
	 * object is nonnull as the parameter {@code o} is nonnull.
	 *
	 * @param <T> type of parameter {@code o}
	 * @param o object that will attempt to turn into an immutable version of itself
	 * @param logger {@link Logger} used for logging
	 * @return
	 * an immutable version of parameter {@code o} if it exists, else parameter
	 * {@code o}, nullable
	 * @see #tryToImmutable overloaded version
	 * @see #tryToImmutableUnboxed unboxed version
	 * @since 0.0.1.0
	 */
	static <T> T tryToImmutableUnboxedNonnull(T o, Logger logger) { return tryToImmutable(o, logger).orElseThrow(Throwables::rethrowCaughtThrowableStatic); }


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
