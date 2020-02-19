package $group__.$modId__.utilities.helpers;

import $group__.$modId__.annotations.IProcessorRuntime;
import $group__.$modId__.utilities.helpers.specific.StringsExtension;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.apache.logging.log4j.Logger;
import sun.corba.Bridge;
import sun.misc.Unsafe;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;

import static $group__.$modId__.utilities.helpers.Assertions.assertNonnull;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxed;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Dynamics.Invocations.Fields.FIELD_MODIFIERS_SETTER;
import static $group__.$modId__.utilities.helpers.PreconditionsExtension.requireRunOnceOnly;
import static $group__.$modId__.utilities.helpers.specific.Loggers.EnumMessages.*;
import static $group__.$modId__.utilities.helpers.specific.MapsExtension.CACHE_EXPIRATION_ACCESS_DURATION;
import static $group__.$modId__.utilities.helpers.specific.MapsExtension.CACHE_EXPIRATION_ACCESS_TIME_UNIT;
import static $group__.$modId__.utilities.helpers.specific.Throwables.*;
import static $group__.$modId__.utilities.variables.Constants.INITIAL_CAPACITY_3;
import static $group__.$modId__.utilities.variables.Constants.MULTI_THREAD_THREAD_COUNT;
import static $group__.$modId__.utilities.variables.Globals.LOGGER;
import static java.lang.invoke.LambdaMetafactory.metafactory;
import static java.lang.invoke.MethodType.methodType;
import static java.util.function.Function.identity;

public enum Dynamics {
	/* MARK empty */;


	/* SECTION static variables */

	public static final Lookup PUBLIC_LOOKUP = MethodHandles.publicLookup();
	public static final Lookup IMPL_LOOKUP;
	@Nullable public static final Unsafe UNSAFE;
	public static final Bridge BRIDGE = Bridge.get();


	public static final LoadingCache<String, org.reflections.Reflections> REFLECTIONS_CACHE = CacheBuilder.newBuilder().initialCapacity(INITIAL_CAPACITY_3).expireAfterAccess(CACHE_EXPIRATION_ACCESS_DURATION, CACHE_EXPIRATION_ACCESS_TIME_UNIT).concurrencyLevel(MULTI_THREAD_THREAD_COUNT).build(CacheLoader.from(t -> {
		org.reflections.Reflections r = new org.reflections.Reflections(t);
		r.expandSuperTypes();
		return r;
	}));


	/* SECTION static methods */

	public static boolean isClassAbstract(Class<?> clazz) { return clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers()); }

	public static boolean isMemberStatic(Member member) { return Modifier.isStatic(member.getModifiers()); }

	public static boolean isMemberFinal(Member member) { return Modifier.isFinal(member.getModifiers()); }


	public static String getPackage(Class<?> clazz) {
		String r = clazz.getName().replace(clazz.getSimpleName(), StringsExtension.STRING_EMPTY);
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
		if (!(overridden.getName().equals(overrider.getName()) && overridden.getReturnType().isAssignableFrom(overrider.getReturnType()))) return false;
		Class<?>[] np = overridden.getParameterTypes(), gp = overrider.getParameterTypes();
		return np.length == gp.length && IntStream.range(0, np.length).allMatch(i -> gp[i].isAssignableFrom(np[i]));
	}


	public static <U> ImmutableSet<Class<? extends U>> getSuperclasses(@Nullable Class<? extends U> clazz) { return getIntermediateSuperclasses(clazz, null); }

	public static <U> ImmutableSet<Class<? extends U>> getThisAndSuperclasses(@Nullable Class<? extends U> clazz) { return getLowerAndIntermediateSuperclasses(clazz, null); }

	public static <U> ImmutableSet<Class<? extends U>> getIntermediateSuperclasses(@Nullable Class<? extends U> lower, @Nullable Class<U> upper) { return getLowerAndIntermediateSuperclasses(castUncheckedUnboxed(lower != null ? lower.getSuperclass() : null), upper); }

	public static <U> ImmutableSet<Class<? extends U>> getLowerAndIntermediateSuperclasses(@Nullable Class<? extends U> lower, @Nullable Class<U> upper) {
		ImmutableSet.Builder<Class<? extends U>> r = new ImmutableSet.Builder<>();
		for (@Nullable Class<?> i = lower; i != upper && i != null; i = i.getSuperclass()) r.add(Casts.<Class<? extends U>>castUncheckedUnboxedNonnull(i));
		return r.build();
	}


	public static ImmutableSet<ImmutableSet<Class<?>>> getSuperclassesAndInterfaces(@Nullable Class<?> clazz) {
		ImmutableSet.Builder<ImmutableSet<Class<?>>> r = new ImmutableSet.Builder<>();
		ImmutableSet<Class<?>> scs = getSuperclasses(clazz);
		r.add(scs);
		scs.forEach(sc -> r.add(ImmutableSet.copyOf(sc.getInterfaces())));
		return r.build();
	}


	static <A extends Annotation> A[] getEffectiveAnnotationsIfInheritingConsidered(IProcessorRuntime<A> processor, Class<?> clazz, Method method, @Nullable Logger logger) {
		Class<A> aClass = processor.annotationType();
		String mName = method.getName();
		Class<?>[] mArgs = method.getParameterTypes();

		A[] r = castUncheckedUnboxedNonnull(Array.newInstance(aClass, 0));
		sss:
		for (ImmutableSet<Class<?>> ss : getSuperclassesAndInterfaces(clazz)) {
			for (Class<?> s : ss) {
				r = tryCall(() -> s.getDeclaredMethod(mName, mArgs), logger).map(t -> t.getDeclaredAnnotationsByType(aClass)).orElse(r);
				consumeIfCaughtThrowable(t -> logger.warn(() -> SUFFIX_WITH_THROWABLE.makeMessage(REFLECTION_UNABLE_TO_GET_MEMBER.makeMessage("method", s, mName, mArgs), t)));
				if (r.length != 0) break sss;
			}
		}

		return r;
	}


	public static Type getGenericSuperclassActualTypeArgument(Class<?> c, int i) throws ClassCastException { return getGenericSuperclassActualTypeArguments(c)[i]; }

	public static Type[] getGenericSuperclassActualTypeArguments(Class<?> c) { return ((ParameterizedType) c.getGenericSuperclass()).getActualTypeArguments(); }


	/* SECTION static initializer */

	static {
		{
			Lookup implLookup = PUBLIC_LOOKUP;
			for (Field f : Lookup.class.getDeclaredFields()) {
				if ("IMPL_LOOKUP".equals(f.getName())) {
					f.setAccessible(true);
					consumeIfCaughtThrowable(t -> SUFFIX_WITH_THROWABLE.makeMessage(REFLECTION_UNABLE_TO_SET_ACCESSIBLE.makeMessage("impl lookup field", f, Lookup.class, true), t));
					implLookup = tryCall(() -> (Lookup) PUBLIC_LOOKUP.unreflectGetter(f).invokeExact(), LOGGER).orElse(implLookup);
					break;
				}
			}
			IMPL_LOOKUP = implLookup;
		}

		{
			@Nullable Unsafe unsafe = null;
			for (Field f : Unsafe.class.getDeclaredFields()) {
				if (f.getType() == Unsafe.class) {
					unsafe = (Unsafe) tryCall(() -> IMPL_LOOKUP.unreflectGetter(f).invokeExact(), LOGGER).orElse(null);
					break;
				}
			}
			UNSAFE = unsafe;
		}
	}


	/* SECTION static classes */

	public enum Reflections {
		/* MARK empty */;


		/* SECTION static classes */

		public enum Bulk {
			/* MARK empty */;


			/* SECTION static methods */

			public static <T> boolean mapFields(Class<T> common, T from, T to, Function<Object, ?> mapper, boolean supers, @Nullable Logger logger) {
				final boolean[] r = {true};
				(supers ? getThisAndSuperclasses(common) : ImmutableList.of(common)).forEach(c -> {
					for (Field f : c.getDeclaredFields()) {
						if (isMemberStatic(f)) continue;

						tryRun(() -> f.setAccessible(true), logger);
						consumeIfCaughtThrowable(t -> logger.warn(() -> SUFFIX_WITH_THROWABLE.makeMessage(REFLECTION_UNABLE_TO_SET_ACCESSIBLE.makeMessage("to-be-copied field", f, c, true), t)));

						@Nullable Object v = tryCall(() -> f.get(from), logger).orElse(null);
						if (caughtThrowableStatic()) {
							logger.warn(() -> SUFFIX_WITH_THROWABLE.makeMessage(REFLECTION_UNABLE_TO_G_SET_FIELD.makeMessage(false, f, from), getCaughtThrowableNonnullStatic()));
							r[0] = false;
						} else {
							int fMod = f.getModifiers() & ~Modifier.FINAL;
							tryRun(() -> FIELD_MODIFIERS_SETTER.invokeExact(f, fMod), logger);
							consumeIfCaughtThrowable(t -> logger.warn(() -> SUFFIX_WITH_THROWABLE.makeMessage(INVOCATION_UNABLE_TO_INVOKE_METHOD_HANDLE.makeMessage(FIELD_MODIFIERS_SETTER, f.toGenericString(), fMod), t)));

							Object vf = mapper.apply(v);
							tryRun(() -> f.set(to, vf), logger);
							consumeIfCaughtThrowable(t -> {
								logger.warn(() -> SUFFIX_WITH_THROWABLE.makeMessage(REFLECTION_UNABLE_TO_G_SET_FIELD.makeMessage(true, f, to, vf), t));
								r[0] = false;
							});
						}
					}
				});
				return r[0];
			}

			public static <T> boolean mapFields(Class<T> common, T from, T to, Function<Object, ?> mapper, @Nullable Logger logger) { return mapFields(common, from, to, mapper, true, logger); }

			public static <T> boolean copyFields(Class<T> common, T from, T to, boolean supers, @Nullable Logger logger) { return mapFields(common, from, to, identity(), supers, logger); }

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

			public static <T, R> Function<T, R> makeFunction(Lookup lookup, MethodHandle method, Class<R> returnType, Class<T> inputType) throws Throwable {
				return castUncheckedUnboxedNonnull(metafactory(lookup, "apply", methodType(Function.class), methodType(Object.class, Object.class), method, methodType(returnType, inputType)).getTarget().invokeExact());
			}

			public static <T, U, R> BiFunction<T, U, R> makeBiFunction(Lookup lookup, MethodHandle method, Class<R> returnType, Class<T> inputType1, Class<U> inputType2) throws Throwable {
				return castUncheckedUnboxedNonnull(metafactory(lookup, "apply", methodType(BiFunction.class), methodType(Object.class, Object.class, Object.class), method, methodType(returnType, inputType1, inputType2)).getTarget().invokeExact());
			}
		}


		public enum Fields {
			/* MARK empty */;


			/* SECTION static methods */

			public static final MethodHandle FIELD_MODIFIERS_SETTER = assertNonnull(tryCall(() -> IMPL_LOOKUP.findGetter(Field.class, "modifiers", int.class), LOGGER).orElseGet(() -> {
				consumeIfCaughtThrowable(t -> LOGGER.warn(() -> SUFFIX_WITH_THROWABLE.makeMessage(INVOCATION_UNABLE_TO_FIND_METHOD_HANDLE.makeMessage("modifiers field", Field.class, "modifiers", null, int.class), t)));
				return null;
			}));
		}
	}


	private static final class SecurityManagerReflections extends SecurityManager {
		/* SECTION static variables */
		
		private static final SecurityManagerReflections INSTANCE = new SecurityManagerReflections();
		
		
		/* SECTION constructors */

		private SecurityManagerReflections() { requireRunOnceOnly(LOGGER); }


		/* SECTION methods */

		@Override
		protected Class<?>[] getClassContext() {
			Class<?>[] r = super.getClassContext();
			return Arrays.copyOfRange(r, 1, r.length);
		}
	}
}
