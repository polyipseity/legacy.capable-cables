package $group__.utilities.helpers;

import $group__.annotations.runtime.processors.IProcessorRuntime;
import $group__.utilities.helpers.specific.Loggers;
import $group__.utilities.helpers.specific.MapsExtension;
import $group__.utilities.helpers.specific.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.misc.Unsafe;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;

import static $group__.utilities.helpers.Capacities.INITIAL_CAPACITY_2;
import static $group__.utilities.helpers.Capacities.INITIAL_CAPACITY_3;
import static $group__.utilities.helpers.Casts.castUncheckedUnboxed;
import static $group__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.utilities.helpers.Dynamics.Invocations.Fields.FIELD_MODIFIERS_SETTER;
import static java.lang.invoke.LambdaMetafactory.metafactory;
import static java.lang.invoke.MethodType.methodType;
import static java.util.Objects.requireNonNull;
import static java.util.function.Function.identity;

public enum Dynamics {
	/* SECTION enums */
	INSTANCE;


	/* SECTION static variables */

	public static final Lookup PUBLIC_LOOKUP = MethodHandles.publicLookup();
	public static final Lookup IMPL_LOOKUP;
	public static final Unsafe UNSAFE;


	public static final LoadingCache<String, org.reflections.Reflections> REFLECTIONS_CACHE =
			CacheBuilder.newBuilder().initialCapacity(INITIAL_CAPACITY_3).expireAfterAccess(MapsExtension.CACHE_EXPIRATION_ACCESS_DURATION, MapsExtension.CACHE_EXPIRATION_ACCESS_TIME_UNIT).concurrencyLevel(Concurrency.MULTI_THREAD_THREAD_COUNT).build(CacheLoader.from(t -> {
				org.reflections.Reflections r = new org.reflections.Reflections(t);
				r.expandSuperTypes();
				return r;
			}));

	private static final Logger LOGGER = LogManager.getLogger(Dynamics.class);


	/* SECTION static methods */

	static {
		{
			Lookup implLookup = PUBLIC_LOOKUP;
			for (Field f : Lookup.class.getDeclaredFields()) {
				if ("IMPL_LOOKUP".equals(f.getName())) {
					f.setAccessible(true);
					Throwables.consumeIfCaughtThrowable(t -> Loggers.EnumMessages.SUFFIX_WITH_THROWABLE.makeMessage(Loggers.EnumMessages.REFLECTION_UNABLE_TO_SET_ACCESSIBLE.makeMessage("impl lookup field", f, Lookup.class, true), t));
					implLookup =
							Throwables.tryCall(() -> (Lookup) PUBLIC_LOOKUP.unreflectGetter(f).invokeExact(), LOGGER).orElse(implLookup);
					break;
				}
			}
			IMPL_LOOKUP = implLookup;
		}

		{
			@Nullable Unsafe unsafe = null;
			for (Field f : Unsafe.class.getDeclaredFields()) {
				if (f.getType() == Unsafe.class) {
					unsafe = (Unsafe) Throwables.tryCall(() -> IMPL_LOOKUP.unreflectGetter(f).invokeExact(), LOGGER).orElse(null);
					break;
				}
			}
			UNSAFE = requireNonNull(unsafe);
		}
	}

	public static boolean isClassAbstract(Class<?> clazz) { return clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers()); }

	public static boolean isMemberStatic(Member member) { return Modifier.isStatic(member.getModifiers()); }

	public static boolean isMemberFinal(Member member) { return Modifier.isFinal(member.getModifiers()); }

	public static String getPackage(Class<?> clazz) {
		String r = clazz.getName().replace(clazz.getSimpleName(), "");
		if (r.endsWith(".")) r = r.substring(0, r.length() - 1);
		return r;
	}

	public static Class<?> getCurrentClass() { return getClassStackTrace(1); }

	public static Class<?> getCallerClass() { return getClassStackTrace(2); }

	public static Class<?> getClassStackTrace(int depth) { return getClassStackTrace()[1 + depth]; }

	public static Class<?>[] getClassStackTrace() {
		Class<?>[] r = SecurityManagerReflections.INSTANCE.getClassContext();
		return Arrays.copyOfRange(r, 1, r.length);
	}

	public static String getMethodNameDescriptor(Method m) { return m.getName() + org.objectweb.asm.Type.getMethodDescriptor(m); }

	public static boolean overrides(Method overrider, Method overridden) {
		if (!(overridden.getName().equals(overrider.getName()) && overridden.getReturnType().isAssignableFrom(overrider.getReturnType())))
			return false;
		Class<?>[] np = overridden.getParameterTypes(), gp = overrider.getParameterTypes();
		return np.length == gp.length && IntStream.range(0, np.length).allMatch(i -> gp[i].isAssignableFrom(np[i]));
	}

	public static <U> ImmutableSet<Class<? extends U>> getSuperclasses(@Nullable Class<? extends U> clazz) { return getIntermediateSuperclasses(clazz, null); }

	public static <U> ImmutableSet<Class<? extends U>> getThisAndSuperclasses(Class<? extends U> clazz) { return getLowerAndIntermediateSuperclasses(clazz, null); }

	public static <U> ImmutableSet<Class<? extends U>> getIntermediateSuperclasses(@Nullable Class<? extends U> lower,
	                                                                               @Nullable Class<U> upper) { return getLowerAndIntermediateSuperclasses(castUncheckedUnboxed(lower != null ? lower.getSuperclass() : null), upper); }

	public static <U> ImmutableSet<Class<? extends U>> getLowerAndIntermediateSuperclasses(@Nullable Class<? extends U> lower, @Nullable Class<U> upper) {
		ImmutableSet.Builder<Class<? extends U>> r = new ImmutableSet.Builder<>();
		for (@Nullable Class<?> i = lower; i != upper && i != null; i = i.getSuperclass())
			r.add(Casts.<Class<? extends U>>castUncheckedUnboxedNonnull(i));
		return r.build();
	}

	public static ImmutableSet<ImmutableSet<Class<?>>> getSuperclassesAndInterfaces(Class<?> clazz) {
		LinkedHashSet<ImmutableSet<Class<?>>> r = new LinkedHashSet<>(INITIAL_CAPACITY_2);

		ImmutableSet<Class<?>> scs = getSuperclasses(clazz);
		r.add(scs);
		AtomicReference<List<Class<?>>> cur = new AtomicReference<>(Arrays.asList(clazz.getInterfaces()));
		scs.forEach(sc -> r.add(ImmutableSet.copyOf(cur.getAndUpdate(c -> {
			List<Class<?>> next = Arrays.asList(sc.getInterfaces());
			c.forEach(t -> Collections.addAll(next, t.getInterfaces()));
			return next;
		}))));
		while (!cur.get().isEmpty()) r.add(ImmutableSet.copyOf(cur.getAndUpdate(s -> {
			List<Class<?>> next = new ArrayList<>(INITIAL_CAPACITY_2);
			s.forEach(t -> Collections.addAll(next, t.getInterfaces()));
			return next;
		})));

		r.removeIf(Collection::isEmpty);
		return ImmutableSet.copyOf(r);
	}

	public static ImmutableSet<ImmutableSet<Class<?>>> getThisAndSuperclassesAndInterfaces(Class<?> type) { return new ImmutableSet.Builder<ImmutableSet<Class<?>>>().add(ImmutableSet.of(type)).addAll(getSuperclassesAndInterfaces(type)).build(); }

	static <A extends Annotation> A[] getEffectiveAnnotationsWithInheritingConsidered(IProcessorRuntime<A> processor, Method method, @Nullable Logger logger) {
		Class<A> aClass = processor.annotationType();
		String mName = method.getName();
		Class<?>[] mArgs = method.getParameterTypes();

		A[] r = method.getDeclaredAnnotationsByType(aClass);
		if (r.length != 0) return r;
		sss:
		for (ImmutableSet<Class<?>> ss : getSuperclassesAndInterfaces(method.getDeclaringClass())) {
			for (Class<?> s : ss) {
				r = Throwables.tryCall(() -> s.getDeclaredMethod(mName, mArgs), logger).map(t -> t.getDeclaredAnnotationsByType(aClass)).orElse(r);
				Throwables.consumeIfCaughtThrowable(t -> logger.warn(() -> Loggers.EnumMessages.SUFFIX_WITH_THROWABLE.makeMessage(Loggers.EnumMessages.REFLECTION_UNABLE_TO_GET_MEMBER.makeMessage("method", s, mName, mArgs), t)));
				if (r.length != 0) break sss;
			}
		}

		return r;
	}

	public static <A extends Annotation> A getEffectiveAnnotationWithInheritingConsidered(IProcessorRuntime<A> processor, Method method, @Nullable Logger logger) throws IllegalArgumentException {
		A[] r = getEffectiveAnnotationsWithInheritingConsidered(processor, method, logger);
		if (r.length != 1) Throwables.rejectArguments(processor.annotationType(), method);
		return r[0];
	}

	public static Type getGenericSuperclassActualTypeArgument(Class<?> c, int i) throws ClassCastException { return getGenericSuperclassActualTypeArguments(c)[i]; }


	/* SECTION static initializer */

	public static Type[] getGenericSuperclassActualTypeArguments(Class<?> c) { return ((ParameterizedType) c.getGenericSuperclass()).getActualTypeArguments(); }


	/* SECTION static classes */

	public enum Reflections {
		/* MARK empty */;


		/* SECTION static classes */

		public enum Bulk {
			/* MARK empty */;


			/* SECTION static methods */

			public static <T> boolean mapFields(Class<T> common, T from, T to, Function<Object, ?> mapper,
			                                    boolean supers, @Nullable Logger logger) {
				final boolean[] r = {true};
				(supers ? getThisAndSuperclasses(common) : ImmutableList.of(common)).forEach(c -> {
					for (Field f : c.getDeclaredFields()) {
						if (isMemberStatic(f)) continue;

						Throwables.tryRun(() -> f.setAccessible(true), logger);
						Throwables.consumeIfCaughtThrowable(t -> logger.warn(() -> Loggers.EnumMessages.SUFFIX_WITH_THROWABLE.makeMessage(Loggers.EnumMessages.REFLECTION_UNABLE_TO_SET_ACCESSIBLE.makeMessage("to-be-copied field", f, c, true), t)));

						@Nullable Object v = Throwables.tryCall(() -> f.get(from), logger).orElse(null);
						if (Throwables.caughtThrowableStatic()) {
							logger.warn(() -> Loggers.EnumMessages.SUFFIX_WITH_THROWABLE.makeMessage(Loggers.EnumMessages.REFLECTION_UNABLE_TO_G_SET_FIELD.makeMessage(false, f, from), Throwables.getCaughtThrowableNonnullStatic()));
							r[0] = false;
						} else {
							int fMod = f.getModifiers() & ~Modifier.FINAL;
							Throwables.tryRun(() -> FIELD_MODIFIERS_SETTER.invokeExact(f, fMod), logger);
							Throwables.consumeIfCaughtThrowable(t -> logger.warn(() -> Loggers.EnumMessages.SUFFIX_WITH_THROWABLE.makeMessage(Loggers.EnumMessages.INVOCATION_UNABLE_TO_INVOKE_METHOD_HANDLE.makeMessage(FIELD_MODIFIERS_SETTER, f.toGenericString(), fMod), t)));

							Object vf = mapper.apply(v);
							Throwables.tryRun(() -> f.set(to, vf), logger);
							Throwables.consumeIfCaughtThrowable(t -> {
								logger.warn(() -> Loggers.EnumMessages.SUFFIX_WITH_THROWABLE.makeMessage(Loggers.EnumMessages.REFLECTION_UNABLE_TO_G_SET_FIELD.makeMessage(true, f, to, vf), t));
								r[0] = false;
							});
						}
					}
				});
				return r[0];
			}

			public static <T> boolean mapFields(Class<T> common, T from, T to, Function<Object, ?> mapper,
			                                    @Nullable Logger logger) {
				return mapFields(common, from, to, mapper,
						true, logger);
			}

			public static <T> boolean copyFields(Class<T> common, T from, T to, boolean supers,
			                                     @Nullable Logger logger) {
				return mapFields(common, from, to,
						identity(), supers, logger);
			}

			@SuppressWarnings("UnusedReturnValue")
			public static <T> boolean copyFields(Class<T> common, T from, T to, @Nullable Logger logger) { return copyFields(common, from, to, true, logger); }
		}
	}

	public enum Invocations {
		/* MARK empty */;


		/* SECTION static classes */

		public enum Lambdas {
			/* MARK empty */;


			/* SECTION static methods */

			public static <T, R> Function<T, R> makeFunction(Lookup lookup, MethodHandle method, Class<R> returnType,
			                                                 Class<T> inputType) throws Throwable {
				return castUncheckedUnboxedNonnull(metafactory(lookup, "apply", methodType(Function.class),
						methodType(Object.class, Object.class), method, methodType(returnType, inputType)).getTarget().invokeExact());
			}

			public static <T, U, R> BiFunction<T, U, R> makeBiFunction(Lookup lookup, MethodHandle method,
			                                                           Class<R> returnType, Class<T> inputType1,
			                                                           Class<U> inputType2) throws Throwable {
				return castUncheckedUnboxedNonnull(metafactory(lookup, "apply", methodType(BiFunction.class),
						methodType(Object.class, Object.class, Object.class), method, methodType(returnType,
								inputType1, inputType2)).getTarget().invokeExact());
			}
		}


		public enum Fields {
			/* MARK empty */;


			/* SECTION static methods */

			public static final MethodHandle FIELD_MODIFIERS_SETTER =
					Assertions.assertNonnull(Throwables.tryCall(() -> IMPL_LOOKUP.findGetter(Field.class, "modifiers", int.class), LOGGER).orElseGet(() -> {
						Throwables.consumeIfCaughtThrowable(t -> LOGGER.warn(() -> Loggers.EnumMessages.SUFFIX_WITH_THROWABLE.makeMessage(Loggers.EnumMessages.INVOCATION_UNABLE_TO_FIND_METHOD_HANDLE.makeMessage("modifiers field", Field.class, "modifiers", null, int.class), t)));
						return null;
					}));
		}
	}


	private static final class SecurityManagerReflections extends SecurityManager {
		/* SECTION static variables */

		private static final SecurityManagerReflections INSTANCE = new SecurityManagerReflections();


		/* SECTION constructors */

		private SecurityManagerReflections() { PreconditionsExtension.requireRunOnceOnly(LOGGER); }


		/* SECTION methods */

		@Override
		protected Class<?>[] getClassContext() {
			Class<?>[] r = super.getClassContext();
			return Arrays.copyOfRange(r, 1, r.length);
		}
	}
}
