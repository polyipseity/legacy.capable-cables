package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.throwable.ThrowableUtilities;

import javax.annotation.Nullable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities.INITIAL_CAPACITY_SMALL;

public enum ClassUtilities {
	;

	private static final MethodHandle DEFINE_CLASS_METHOD_HANDLE;

	static {
		try {
			DEFINE_CLASS_METHOD_HANDLE = InvokeUtilities.IMPL_LOOKUP.findVirtual(ClassLoader.class, "defineClass",
					MethodType.methodType(Class.class, String.class, byte[].class, int.class, int.class));
		} catch (NoSuchMethodException | IllegalAccessException e) {
			throw ThrowableUtilities.propagate(e);
		}
	}

	public static <U> ImmutableSet<Class<? extends U>> getThisAndSuperclasses(Class<? extends U> clazz) { return getLowerAndIntermediateSuperclasses(clazz, null); }

	public static <U> ImmutableSet<Class<? extends U>> getLowerAndIntermediateSuperclasses(@Nullable Class<? extends U> lower, @Nullable Class<U> upper) {
		ImmutableSet.Builder<Class<? extends U>> r = new ImmutableSet.Builder<>();
		for (@Nullable Class<?> i = lower; !Objects.equals(i, upper) && i != null; i = i.getSuperclass())
			r.add(CastUtilities.<Class<? extends U>>castUnchecked(i));
		return r.build();
	}

	@SuppressWarnings("UnstableApiUsage")
	public static Set<Field> getAllFields(Class<?> clazz) {
		return getThisAndSuperclassesAndInterfaces(clazz).stream().sequential()
				.flatMap(Collection::stream)
				.map(Class::getDeclaredFields)
				.flatMap(Arrays::stream)
				.collect(ImmutableSet.toImmutableSet());
	}

	public static ImmutableSet<ImmutableSet<Class<?>>> getThisAndSuperclassesAndInterfaces(Class<?> type) { return new ImmutableSet.Builder<ImmutableSet<Class<?>>>().add(ImmutableSet.of(type)).addAll(getSuperclassesAndInterfaces(type)).build(); }

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

	public static <U> ImmutableSet<Class<? extends U>> getSuperclasses(@Nullable Class<? extends U> clazz) { return getIntermediateSuperclasses(clazz, null); }

	public static <U> ImmutableSet<Class<? extends U>> getIntermediateSuperclasses(@Nullable Class<? extends U> lower,
	                                                                               @Nullable Class<U> upper) {
		return getLowerAndIntermediateSuperclasses(
				CastUtilities.castUncheckedNullable(
						Optional.ofNullable(lower)
								.map(Class::getSuperclass)
								.orElse(null)), upper);
	}

	public static Optional<Field> getAnyField(Class<?> clazz, String name) {
		@Nullable Field ret = null;
		find:
		for (ImmutableSet<Class<?>> hierarchy : ClassUtilities.getThisAndSuperclassesAndInterfaces(clazz)) {
			for (Class<?> class2 : hierarchy) {
				try {
					ret = class2.getDeclaredField(name);
					break find;
				} catch (NoSuchFieldException ignored) {}
			}
		}
		return Optional.ofNullable(ret);
	}

	public static Optional<Method> getAnyMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
		@Nullable Method ret = null;
		find:
		for (ImmutableSet<Class<?>> hierarchy : ClassUtilities.getThisAndSuperclassesAndInterfaces(clazz)) {
			for (Class<?> class2 : hierarchy) {
				try {
					ret = class2.getDeclaredMethod(name, parameterTypes);
					break find;
				} catch (NoSuchMethodException ignored) {}
			}
		}
		return Optional.ofNullable(ret);
	}

	@SuppressWarnings("UnstableApiUsage")
	public static Set<Method> getAllMethods(Class<?> clazz) {
		return getThisAndSuperclassesAndInterfaces(clazz).stream().sequential()
				.flatMap(Collection::stream)
				.map(Class::getDeclaredMethods)
				.flatMap(Arrays::stream)
				.collect(ImmutableSet.toImmutableSet());
	}

	public static Class<?> defineClass(ClassLoader classLoader, String name, byte[] data) {
		// TODO Java 9 - use Lookup.defineClass
		try {
			return (Class<?>) DEFINE_CLASS_METHOD_HANDLE.invokeExact(classLoader, name, data, 0, data.length);
		} catch (Throwable throwable) {
			throw ThrowableUtilities.propagate(throwable);
		}
	}

	public static String getPackage(Class<?> clazz) {
		String r = clazz.getName().replace(clazz.getSimpleName(), "");
		if (r.endsWith(".")) r = r.substring(0, r.length() - 1);
		return r;
	}

	public static boolean isClassAbstract(Class<?> clazz) { return clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers()); }
}
