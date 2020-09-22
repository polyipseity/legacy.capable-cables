package $group__.utilities;

import $group__.utilities.templates.CommonConfigurationTemplate;
import $group__.utilities.throwable.ThrowableUtilities;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.jodah.typetools.TypeResolver;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Marker;
import sun.misc.Unsafe;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.*;
import java.util.stream.IntStream;

import static $group__.utilities.CapacityUtilities.INITIAL_CAPACITY_SMALL;

public enum DynamicUtilities {
	;

	public static final Lookup PUBLIC_LOOKUP = MethodHandles.publicLookup();
	public static final Lookup IMPL_LOOKUP;
	public static final Unsafe UNSAFE;

	private static final Marker CLASS_MARKER = UtilitiesMarkers.getInstance().getClassMarker(DynamicUtilities.class);
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UtilitiesConfiguration.getInstance());
	private static final int TRUSTED_LOOKUP_MODES = 15;

	static {
		IMPL_LOOKUP = Arrays.stream(Lookup.class.getDeclaredFields()).unordered()
				.filter(f -> Lookup.class.equals(f.getType()))
				.map(f -> {
					f.setAccessible(true);
					Lookup ret;
					try {
						ret = (Lookup) PUBLIC_LOOKUP.unreflectGetter(f).invokeExact();
					} catch (Throwable throwable) {
						throw ThrowableUtilities.propagate(throwable);
					}
					f.setAccessible(false);
					return ret;
				})
				.filter(lookup -> lookup.lookupModes() == TRUSTED_LOOKUP_MODES)
				.findAny()
				.orElseThrow(() -> ThrowableUtilities.propagate(new NoSuchFieldException()));

		UNSAFE = Arrays.stream(Unsafe.class.getDeclaredFields()).unordered()
				.filter(f -> Unsafe.class.equals(f.getType()))
				.map(f -> {
					try {
						return (Unsafe) IMPL_LOOKUP.unreflectGetter(f).invokeExact();
					} catch (Throwable throwable) {
						throw ThrowableUtilities.propagate(throwable);
					}
				})
				.findAny()
				.orElseThrow(() -> ThrowableUtilities.propagate(new NoSuchFieldException()));

		try {
			DEFINE_CLASS_METHOD_HANDLE = IMPL_LOOKUP.findVirtual(ClassLoader.class, "defineClass",
					MethodType.methodType(Class.class, String.class, byte[].class, int.class, int.class));
		} catch (NoSuchMethodException | IllegalAccessException e) {
			throw ThrowableUtilities.propagate(e);
		}
	}

	public static Marker getClassMarker() { return CLASS_MARKER; }

	private static final MethodHandle DEFINE_CLASS_METHOD_HANDLE;

	public static <A extends Annotation> A getEffectiveAnnotationWithInheritingConsidered(Class<A> annotationType, Method method) throws IllegalArgumentException {
		A[] r = getEffectiveAnnotationsWithInheritingConsidered(annotationType, method);
		if (r.length != 1)
			throw new IllegalArgumentException(
					new LogMessageBuilder()
							.addMarkers(ProcessorUtilities::getClassMarker)
							.addKeyValue("annotationType", annotationType).addKeyValue("method", method)
							.addMessages(() -> getResourceBundle().getString("annotations.get.plural.fail"))
							.build()
			);
		return r[0];
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
		Class<?>[] r = SecurityManagerHolder.INSTANCE.getClassContext();
		return Arrays.copyOfRange(r, 1, r.length);
	}

	public static Class<?> getCallerClass() { return getClassStackTrace(2); }

	public static Optional<StackTraceElement> getCallerStackTraceElement() {
		/* COMMENT
		0 - this method
		1 - caller of this method
		2 - caller of the caller of this method
		 */
		return Optional.of(getCurrentStackTraceElements())
				.filter(stacktrace -> stacktrace.length > 2)
				.map(stacktrace -> stacktrace[2]);
	}

	public static StackTraceElement[] getCurrentStackTraceElements() {
		StackTraceElement[] ret = ThrowableUtilities.create().getStackTrace();
		/* COMMENT
		0 - create()
		1 - this method
		2 - caller of this method
		 */
		return Arrays.copyOfRange(ret, Math.min(2, ret.length), ret.length);
	}

	public static Optional<StackTraceElement> getCurrentStackTraceElement() {
		/* COMMENT
		0 - this method
		1 - caller of this method
		 */
		return Optional.of(getCurrentStackTraceElements())
				.filter(stacktrace -> stacktrace.length > 1)
				.map(stacktrace -> stacktrace[1]);
	}

	public static String getCurrentStackTraceString() { return ExceptionUtils.getStackTrace(ThrowableUtilities.create()); }

	@SuppressWarnings("ObjectAllocationInLoop")
	static <A extends Annotation> A[] getEffectiveAnnotationsWithInheritingConsidered(Class<A> annotationType, Method method) {
		String mName = method.getName();
		Class<?>[] mArgs = method.getParameterTypes();

		A[] r = method.getDeclaredAnnotationsByType(annotationType);
		if (r.length != 0) return r;
		sss:
		for (ImmutableSet<Class<?>> ss : getSuperclassesAndInterfaces(method.getDeclaringClass())) {
			for (Class<?> s : ss) {
				r = ThrowableUtilities.getQuietly(() -> s.getDeclaredMethod(mName, mArgs), NoSuchMethodException.class, UtilitiesConfiguration.getInstance().getThrowableHandler())
						.map(t -> t.getDeclaredAnnotationsByType(annotationType))
						.orElse(r);
				if (r.length != 0) break sss;
			}
		}

		return r;
	}

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

	public static Class<?> defineClass(ClassLoader classLoader, String name, byte[] data) {
		// TODO Java 9 - use Lookup.defineClass
		try {
			return (Class<?>) DEFINE_CLASS_METHOD_HANDLE.invokeExact(classLoader, name, data, 0, data.length);
		} catch (Throwable throwable) {
			throw ThrowableUtilities.propagate(throwable);
		}
	}

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	@SuppressWarnings("ObjectAllocationInLoop")
	public static ImmutableSet<ImmutableSet<Class<?>>> getSuperclassesAndInterfaces(Class<?> clazz) {
		Set<ImmutableSet<Class<?>>> ret = new LinkedHashSet<>(INITIAL_CAPACITY_SMALL);

		ImmutableSet<Class<?>> scs = getSuperclasses(clazz);
		ret.add(scs);
		AtomicReference<List<Class<?>>> cur = new AtomicReference<>(Arrays.asList(clazz.getInterfaces()));
		scs.forEach(sc -> {
			List<Class<?>> next = ImmutableList.copyOf(sc.getInterfaces());
			ret.add(ImmutableSet.copyOf(AssertionUtilities.assertNonnull(cur.getAndUpdate(c -> {
				List<Class<?>> n = new ArrayList<>(next.size() + c.size() * CapacityUtilities.INITIAL_CAPACITY_TINY);
				n.addAll(next);
				c.forEach(t ->
						Collections.addAll(n, t.getInterfaces()));
				return n;
			}))));
		});
		while (!AssertionUtilities.assertNonnull(cur.get()).isEmpty()) {
			ret.add(ImmutableSet.copyOf(AssertionUtilities.assertNonnull(cur.getAndUpdate(s -> {
				List<Class<?>> n = new ArrayList<>(s.size() * CapacityUtilities.INITIAL_CAPACITY_TINY);
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

		public enum LambdaMetaFactoryUtilities {
			;

			@SuppressWarnings("unchecked")
			public static <T> Consumer<T> makeConsumer(Lookup lookup, MethodHandle method,
			                                           Class<T> inputType) throws Throwable {
				return (Consumer<T>) LambdaMetafactory.metafactory(lookup,
						"accept",
						MethodType.methodType(Consumer.class),
						MethodType.methodType(void.class, Object.class),
						method,
						MethodType.methodType(void.class, inputType))
						.getTarget()
						.invokeExact();
			}

			@SuppressWarnings("unchecked")
			public static <T, U> BiConsumer<T, U> makeBiConsumer(Lookup lookup, MethodHandle method,
			                                                     Class<T> inputType1, Class<U> inputType2) throws Throwable {
				return (BiConsumer<T, U>) LambdaMetafactory.metafactory(lookup,
						"accept",
						MethodType.methodType(BiConsumer.class),
						MethodType.methodType(void.class, Object.class, Object.class),
						method,
						MethodType.methodType(void.class, inputType1, inputType2))
						.getTarget()
						.invokeExact();
			}

			@SuppressWarnings("unchecked")
			public static <T, R> Function<T, R> makeFunction(Lookup lookup, MethodHandle method,
			                                                 Class<R> returnType, Class<T> inputType) throws Throwable {
				return (Function<T, R>) LambdaMetafactory.metafactory(lookup,
						"apply",
						MethodType.methodType(Function.class),
						MethodType.methodType(Object.class, Object.class),
						method,
						MethodType.methodType(returnType, inputType))
						.getTarget()
						.invokeExact();
			}

			@SuppressWarnings("unchecked")
			public static <T, U, R> BiFunction<T, U, R> makeBiFunction(Lookup lookup, MethodHandle method,
			                                                           Class<R> returnType, Class<T> inputType1, Class<U> inputType2) throws Throwable {
				return (BiFunction<T, U, R>) LambdaMetafactory.metafactory(lookup,
						"apply",
						MethodType.methodType(BiFunction.class),
						MethodType.methodType(Object.class, Object.class, Object.class),
						method,
						MethodType.methodType(returnType, inputType1, inputType2))
						.getTarget()
						.invokeExact();
			}
		}
	}

	public static final class SecurityManagerHolder
			extends SecurityManager {
		public static final SecurityManagerHolder INSTANCE = new SecurityManagerHolder();

		private SecurityManagerHolder() { PreconditionUtilities.requireRunOnceOnly(); }

		@Override
		public Class<?>[] getClassContext() {
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
