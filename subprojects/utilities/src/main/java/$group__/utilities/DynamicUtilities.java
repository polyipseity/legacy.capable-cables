package $group__.utilities;

import $group__.utilities.ThrowableUtilities.BecauseOf;
import $group__.utilities.ThrowableUtilities.ThrowableCatcher;
import $group__.utilities.ThrowableUtilities.Try;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.jodah.typetools.TypeResolver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import sun.misc.Unsafe;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import static $group__.utilities.CapacityUtilities.INITIAL_CAPACITY_SMALL;
import static $group__.utilities.LoggerUtilities.EnumMessages.*;
import static java.lang.invoke.LambdaMetafactory.metafactory;
import static java.lang.invoke.MethodType.methodType;

public enum DynamicUtilities {
	INSTANCE;

	public static final Lookup PUBLIC_LOOKUP = MethodHandles.publicLookup();
	public static final Lookup IMPL_LOOKUP;
	public static final Unsafe UNSAFE;


	public static final LoadingCache<String, Reflections> REFLECTIONS_CACHE =
			CacheBuilder.newBuilder().initialCapacity(INITIAL_CAPACITY_SMALL).expireAfterAccess(MapUtilities.CACHE_EXPIRATION_ACCESS_DURATION, MapUtilities.CACHE_EXPIRATION_ACCESS_TIME_UNIT).concurrencyLevel(ConcurrencyUtilities.MULTI_THREAD_THREAD_COUNT).build(CacheLoader.from(t -> {
				Reflections r = new Reflections(t);
				r.expandSuperTypes();
				return r;
			}));

	private static final Logger LOGGER = LogManager.getLogger(DynamicUtilities.class);

	private static final int TRUSTED_LOOKUP_MODES = 15;

	static {
		IMPL_LOOKUP = Arrays.stream(Lookup.class.getDeclaredFields()).unordered()
				.filter(f -> Lookup.class.equals(f.getType()))
				.map(f -> {
					Try.run(() -> f.setAccessible(true), LOGGER);
					ThrowableCatcher.acceptIfCaught(t -> LOGGER.warn(() -> SUFFIX_WITH_THROWABLE.makeMessage(REFLECTION_UNABLE_TO_SET_ACCESSIBLE.makeMessage("impl lookup field", f, Lookup.class, true), t)));

					@Nullable Optional<Lookup> ret = Try.call(() -> (Lookup) PUBLIC_LOOKUP.unreflectGetter(f).invokeExact(), LOGGER).filter(l -> l.lookupModes() == TRUSTED_LOOKUP_MODES);

					Try.run(() -> f.setAccessible(false), LOGGER);
					return ret;
				})
				.filter(Optional::isPresent)
				.map(Optional::get)
				.findAny()
				.orElse(PUBLIC_LOOKUP);

		UNSAFE = Arrays.stream(Unsafe.class.getDeclaredFields()).unordered()
				.filter(f -> Unsafe.class.equals(f.getType()))
				.map(f -> Try.call(() -> (Unsafe) IMPL_LOOKUP.unreflectGetter(f).invokeExact(), LOGGER))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.findAny()
				.orElseThrow(InternalError::new);
	}

	public static boolean isClassAbstract(Class<?> clazz) { return clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers()); }

	public static String getPackage(Class<?> clazz) {
		String r = clazz.getName().replace(clazz.getSimpleName(), "");
		if (r.endsWith(".")) r = r.substring(0, r.length() - 1);
		return r;
	}

	public static Class<?> getCurrentClass() { return getClassStackTrace(1); }

	public static Class<?> getClassStackTrace(int depth) { return getClassStackTrace()[1 + depth]; }

	public static Class<?>[] getClassStackTrace() {
		Class<?>[] r = SecurityManagerReflections.INSTANCE.getClassContext();
		return Arrays.copyOfRange(r, 1, r.length);
	}

	public static Class<?> getCallerClass() { return getClassStackTrace(2); }

	public static String getMethodNameDescriptor(Method m) { return m.getName() + org.objectweb.asm.Type.getMethodDescriptor(m); }

	public static boolean overrides(Method overrider, Method overridden) {
		if (!(overridden.getName().equals(overrider.getName()) && overridden.getReturnType().isAssignableFrom(overrider.getReturnType())))
			return false;
		Class<?>[] np = overridden.getParameterTypes(), gp = overrider.getParameterTypes();
		return np.length == gp.length && IntStream.range(0, np.length).allMatch(i -> gp[i].isAssignableFrom(np[i]));
	}

	public static <U> ImmutableSet<Class<? extends U>> getThisAndSuperclasses(Class<? extends U> clazz) { return getLowerAndIntermediateSuperclasses(clazz, null); }

	public static ImmutableSet<ImmutableSet<Class<?>>> getThisAndSuperclassesAndInterfaces(Class<?> type) { return new ImmutableSet.Builder<ImmutableSet<Class<?>>>().add(ImmutableSet.of(type)).addAll(getSuperclassesAndInterfaces(type)).build(); }

	public static <U> ImmutableSet<Class<? extends U>> getIntermediateSuperclasses(@Nullable Class<? extends U> lower,
	                                                                               @Nullable Class<U> upper) {
		return getLowerAndIntermediateSuperclasses(
				CastUtilities.castUncheckedNullable(
						Optional.ofNullable(lower)
								.map(Class::getSuperclass)
								.orElse(null)), upper);
	}

	public static <U> ImmutableSet<Class<? extends U>> getSuperclasses(@Nullable Class<? extends U> clazz) { return getIntermediateSuperclasses(clazz, null); }

	public static <U> ImmutableSet<Class<? extends U>> getLowerAndIntermediateSuperclasses(@Nullable Class<? extends U> lower, @Nullable Class<U> upper) {
		ImmutableSet.Builder<Class<? extends U>> r = new ImmutableSet.Builder<>();
		for (@Nullable Class<?> i = lower; !Objects.equals(i, upper) && i != null; i = i.getSuperclass())
			r.add(CastUtilities.<Class<? extends U>>castUnchecked(i));
		return r.build();
	}

	@SuppressWarnings("ObjectAllocationInLoop")
	static <A extends Annotation> A[] getEffectiveAnnotationsWithInheritingConsidered(Class<A> annotationType, Method method, @Nullable Logger logger) {
		String mName = method.getName();
		Class<?>[] mArgs = method.getParameterTypes();

		A[] r = method.getDeclaredAnnotationsByType(annotationType);
		if (r.length != 0) return r;
		sss:
		for (ImmutableSet<Class<?>> ss : getSuperclassesAndInterfaces(method.getDeclaringClass())) {
			for (Class<?> s : ss) {
				r = Try.call(() -> s.getDeclaredMethod(mName, mArgs), logger)
						.map(t -> t.getDeclaredAnnotationsByType(annotationType))
						.orElse(r);
				if (logger != null)
					ThrowableCatcher.acceptIfCaught(t ->
							logger.warn(() ->
									SUFFIX_WITH_THROWABLE.makeMessage(
											REFLECTION_UNABLE_TO_GET_MEMBER.makeMessage("method", s, mName, mArgs), t)));
				if (r.length != 0) break sss;
			}
		}

		return r;
	}

	public static <A extends Annotation> A getEffectiveAnnotationWithInheritingConsidered(Class<A> annotationType, Method method, @Nullable Logger logger) throws IllegalArgumentException {
		A[] r = getEffectiveAnnotationsWithInheritingConsidered(annotationType, method, logger);
		if (r.length != 1) throw BecauseOf.illegalArgument("Too many or not enough annotations",
				"annotationType", annotationType,
				"method", method);
		return r[0];
	}

	@SuppressWarnings("ObjectAllocationInLoop")
	public static ImmutableSet<ImmutableSet<Class<?>>> getSuperclassesAndInterfaces(Class<?> clazz) {
		Set<ImmutableSet<Class<?>>> ret = new LinkedHashSet<>(INITIAL_CAPACITY_SMALL);

		ImmutableSet<Class<?>> scs = getSuperclasses(clazz);
		ret.add(scs);
		AtomicReference<List<Class<?>>> cur = new AtomicReference<>(Arrays.asList(clazz.getInterfaces()));
		scs.forEach(sc -> {
			List<Class<?>> next = Arrays.asList(sc.getInterfaces());
			ret.add(ImmutableSet.copyOf(AssertionUtilities.assertNonnull(cur.getAndUpdate(c -> {
				List<Class<?>> n = new LinkedList<>(next);
				c.forEach(t ->
						Collections.addAll(n, t.getInterfaces()));
				return n;
			}))));
		});
		while (!AssertionUtilities.assertNonnull(cur.get()).isEmpty()) {
			ret.add(ImmutableSet.copyOf(AssertionUtilities.assertNonnull(cur.getAndUpdate(s -> {
				List<Class<?>> n = new LinkedList<>();
				s.forEach(t ->
						Collections.addAll(n, t.getInterfaces()));
				return n;
			}))));
		}

		ret.removeIf(Collection::isEmpty);
		return ImmutableSet.copyOf(ret);
	}

	@SuppressWarnings("UnstableApiUsage")
	public static Set<Field> getAllFields(Class<?> clazz) {
		return getThisAndSuperclasses(clazz).stream().unordered()
				.flatMap(c -> Sets.newHashSet(c.getDeclaredFields()).stream()).collect(ImmutableSet.toImmutableSet());
	}

	public enum InvocationUtilities {
		;

		@SuppressWarnings("SpellCheckingInspection")
		public enum LambdaMetafactoryUtilities {
			;

			@SuppressWarnings("unchecked")
			public static <T, R> Function<T, R> makeFunction(Lookup lookup, MethodHandle method, Class<R> returnType,
			                                                 Class<T> inputType) throws Throwable {
				return (Function<T, R>) metafactory(lookup, "apply", methodType(Function.class),
						methodType(Object.class, Object.class), method, methodType(returnType, inputType)).getTarget().invokeExact();
			}

			@SuppressWarnings("unchecked")
			public static <T, U, R> BiFunction<T, U, R> makeBiFunction(Lookup lookup, MethodHandle method,
			                                                           Class<R> returnType, Class<T> inputType1,
			                                                           Class<U> inputType2) throws Throwable {
				return (BiFunction<T, U, R>) metafactory(lookup, "apply", methodType(BiFunction.class),
						methodType(Object.class, Object.class, Object.class), method, methodType(returnType,
								inputType1, inputType2)).getTarget().invokeExact();
			}
		}
	}


	private static final class SecurityManagerReflections extends SecurityManager {
		private static final SecurityManagerReflections INSTANCE = new SecurityManagerReflections();

		@Override
		protected Class<?>[] getClassContext() {
			Class<?>[] r = super.getClassContext();
			return Arrays.copyOfRange(r, 1, r.length);
		}
	}

	public enum Extensions {
		;

		@SuppressWarnings("unchecked")
		public static Optional<Class<?>>[] wrapTypeResolverResults(Class<?>... results) {
			return Arrays.stream(results).sequential()
					.map(Extensions::wrapTypeResolverResult)
					.toArray(Optional[]::new);
		}

		public static Optional<Class<?>> wrapTypeResolverResult(@Nullable Class<?> result) {
			return Optional.<Class<?>>ofNullable(result)
					.filter(Predicate.isEqual(TypeResolver.Unknown.class).negate());
		}
	}
}
