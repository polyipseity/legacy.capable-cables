package $group__.$modId__.utilities.helpers;

import $group__.$modId__.utilities.helpers.specific.StringsExtension;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.Logger;
import sun.corba.Bridge;
import sun.misc.Unsafe;

import javax.annotation.Nullable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;

import static $group__.$modId__.utilities.helpers.Assertions.assertNonnull;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Dynamics.Invocations.Fields.FIELD_MODIFIERS_SETTER;
import static $group__.$modId__.utilities.helpers.PreconditionsExtension.requireRunOnceOnly;
import static $group__.$modId__.utilities.helpers.specific.Loggers.EnumMessages.*;
import static $group__.$modId__.utilities.helpers.specific.MapsExtension.CACHE_EXPIRATION_ACCESS_DURATION;
import static $group__.$modId__.utilities.helpers.specific.MapsExtension.CACHE_EXPIRATION_ACCESS_TIME_UNIT;
import static $group__.$modId__.utilities.helpers.specific.Throwables.*;
import static $group__.$modId__.utilities.variables.Constants.*;
import static $group__.$modId__.utilities.variables.Globals.LOGGER;
import static java.lang.invoke.LambdaMetafactory.metafactory;
import static java.lang.invoke.MethodType.methodType;

public enum Dynamics {
	/* MARK empty */;


	/* SECTION static variables */

	public static final Lookup PUBLIC_LOOKUP = MethodHandles.publicLookup();
	public static final Lookup IMPL_LOOKUP;
	@Nullable public static final Unsafe UNSAFE;
	public static final Bridge BRIDGE = Bridge.get();


	public static final LoadingCache<String, org.reflections.Reflections> REFLECTIONS_CACHE = CacheBuilder.newBuilder().initialCapacity(INITIAL_SIZE_MEDIUM).expireAfterAccess(CACHE_EXPIRATION_ACCESS_DURATION, CACHE_EXPIRATION_ACCESS_TIME_UNIT).concurrencyLevel(MULTI_THREAD_THREAD_COUNT).build(CacheLoader.from(t -> {
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

	public static boolean isFormerMethodOverriddenByLatter(Method a, Method b) {
		if (!(a.getName().equals(b.getName()) && a.getReturnType().isAssignableFrom(b.getReturnType()))) return false;
		Class<?>[] ap = a.getParameterTypes(), bp = b.getParameterTypes();
		return ap.length == bp.length && IntStream.range(0, ap.length).allMatch(i -> bp[i].isAssignableFrom(ap[i]));
	}


	public static <U> LinkedHashSet<Class<? extends U>> getSuperclasses(Class<? extends U> lower) { return getIntermediateSuperclasses(lower, null); }

	public static <U> LinkedHashSet<Class<? extends U>> getThisAndSuperclasses(Class<? extends U> lower) { return getLowerAndIntermediateSuperclasses(lower, null); }

	public static <U> LinkedHashSet<Class<? extends U>> getIntermediateSuperclasses(Class<? extends U> lower, @Nullable Class<U> upper) {
		LinkedHashSet<Class<? extends U>> r = getLowerAndIntermediateSuperclasses(lower, upper);
		r.remove(lower);
		return r;
	}

	public static <U> LinkedHashSet<Class<? extends U>> getLowerAndIntermediateSuperclasses(Class<? extends U> lower, @Nullable Class<U> upper) {
		LinkedHashSet<Class<? extends U>> r = new LinkedHashSet<>(INITIAL_SIZE_SMALL);
		for (@Nullable Class<?> i = lower; i != upper && i != null; i = i.getSuperclass()) r.add(castUncheckedUnboxedNonnull(i));
		return r;
	}


	public static LinkedHashSet<LinkedHashSet<Class<?>>> getSuperclassesAndInterfaces(Class<?> c) {
		LinkedHashSet<LinkedHashSet<Class<?>>> r = new LinkedHashSet<>(INITIAL_SIZE_SMALL);
		@Nullable Class<?> cs = c.getSuperclass();
		LinkedHashSet<Class<?>> l = new LinkedHashSet<>(cs == null ? Arrays.asList(c.getInterfaces()) : Lists.asList(cs, c.getInterfaces()));
		while (!l.isEmpty()) {
			r.add(l);
			LinkedHashSet<Class<?>> l1 = new LinkedHashSet<>(INITIAL_SIZE_SMALL);
			l.forEach(t -> {
				@Nullable Class<?> ts = t.getSuperclass();
				l1.addAll(ts == null ? Arrays.asList(t.getInterfaces()) : Lists.asList(ts, t.getInterfaces()));
			});
			l = l1;
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

			public static <T> boolean mapFields(Class<T> common, T from, T to, Function<Object, ?> mapper, boolean supers, Logger logger) {
				final boolean[] r = {true};
				(supers ? getThisAndSuperclasses(common) : ImmutableList.of(common)).forEach(c -> {
					for (Field f : c.getDeclaredFields()) {
						if (isMemberStatic(f)) continue;

						tryRun(() -> f.setAccessible(true), logger);
						consumeIfCaughtThrowable(t -> logger.warn(() -> SUFFIX_WITH_THROWABLE.makeMessage(REFLECTION_UNABLE_TO_SET_ACCESSIBLE.makeMessage("to-be-copied field", f, c, true), t)));

						@Nullable Object v = tryCall(() -> f.get(from), logger).orElse(null);
						if (caughtThrowableStatic()) {
							logger.warn(() -> SUFFIX_WITH_THROWABLE.makeMessage(REFLECTION_UNABLE_TO_G_SET_FIELD.makeMessage(false, f, from), getCaughtThrowableUnboxedNonnullStatic()));
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

			public static <T> boolean mapFields(Class<T> common, T from, T to, Function<Object, ?> mapper, Logger logger) { return mapFields(common, from, to, mapper, true, logger); }

			public static <T> boolean copyFields(Class<T> common, T from, T to, boolean supers, Logger logger) { return mapFields(common, from, to, Function.identity(), supers, logger); }

			@SuppressWarnings("UnusedReturnValue")
			public static <T> boolean copyFields(Class<T> common, T from, T to, Logger logger) { return copyFields(common, from, to, true, logger); }
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
