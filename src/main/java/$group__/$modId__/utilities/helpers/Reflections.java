package $group__.$modId__.utilities.helpers;

import $group__.$modId__.utilities.constructs.interfaces.basic.IAdapter;
import com.google.common.collect.Lists;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;

import javax.annotation.Nullable;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import static $group__.$modId__.utilities.helpers.Casts.castUnchecked;
import static $group__.$modId__.utilities.helpers.Masks.maskToNonnull;
import static $group__.$modId__.utilities.helpers.Throwables.rejectArguments;
import static java.util.Objects.requireNonNull;

public enum Reflections {
	/* MARK empty */ ;


	/* SECTION static methods */

	public static boolean isClassAbstract(Class<?> c) { return c.isInterface() || Modifier.isAbstract(c.getModifiers()); }

	public static boolean isMemberStatic(Member m) { return Modifier.isStatic(m.getModifiers()); }

	public static boolean isMemberFinal(Member m) { return Modifier.isFinal(m.getModifiers()); }


	public static SetMultimap<Long, Class<?>> getSuperclassesAndInterfaces(Class<?> c) {
		@SuppressWarnings("UnstableApiUsage") SetMultimap<Long, Class<?>> r = MultimapBuilder.hashKeys().linkedHashSetValues().build();
		Class<?> cs = c.getSuperclass();
		List<Class<?>> l = cs == null ? Arrays.asList(c.getInterfaces()) : Lists.asList(c.getSuperclass(), c.getInterfaces());
		long k = 0;
		while (!l.isEmpty()) {
			r.putAll(k++, l);
			List<Class<?>> tmpL = new LinkedList<>();
			l.forEach(t -> {
				Class<?> ts = t.getSuperclass();
				tmpL.addAll(ts == null ? Arrays.asList(t.getInterfaces()) : Lists.asList(t.getSuperclass(), t.getInterfaces()));
			});
			l = tmpL;
		}
		return r;
	}


	public static String getMethodNameDescriptor(Method m) { return m.getName() + org.objectweb.asm.Type.getMethodDescriptor(m); }


	public static boolean isFormerMethodOverriddenByLatter(Method a, Method b) {
		if (!(a.getName().equals(b.getName()) && a.getReturnType().isAssignableFrom(b.getReturnType()))) return false;
		Class<?>[] ap = a.getParameterTypes();
		Class<?>[] bp = a.getParameterTypes();
		if (ap.length != bp.length) return false;
		for (int i = 0; i < ap.length; i++) { if (!bp[i].isAssignableFrom(ap[i])) return false; }
		return true;
	}


	public static Type[] getGenericSuperclassActualTypeArguments(Class<?> c) { return ((ParameterizedType) c.getGenericSuperclass()).getActualTypeArguments(); }

	public static <T extends Type> T getGenericSuperclassActualTypeArgument(Class<?> c, int i) { return castUnchecked(getGenericSuperclassActualTypeArguments(c)[i]); }


	/* SECTION static classes */

	public enum Unsafe {
		/* MARK empty */ ;


		/* SECTION static methods */

		@Nullable
		public static Class<?> forName(BiFunction<? super Throwable, ? super String, ? extends BiFunction<? super Boolean, ? super ClassLoader, ? extends Class<?>>> callback, String name, boolean initialize, ClassLoader loader) { try { return Class.forName(name, initialize, loader); } catch (Throwable t) { return callback.apply(t, name).apply(initialize, loader); } }

		public static Class<?> forNameNonnull(BiFunction<? super Throwable, ? super String, ? extends BiFunction<? super Boolean, ? super ClassLoader, ? extends Class<?>>> callback, String name, boolean initialize, ClassLoader loader) { return requireNonNull(forName(callback, name, initialize, loader)); }

		public static Class<?> forName(String name, boolean initialize, ClassLoader loader) { return maskToNonnull(forName((t, u) -> (v, w) -> { throw rejectArguments(t, u, v, w); }, name, initialize, loader)); }

		@Nullable
		public static Class<?> forName(BiFunction<? super Throwable, ? super String, ? extends Function<? super Boolean, ? extends Class<?>>> callback, String name, boolean initialize) { return forName((t, u) -> (v, w) -> callback.apply(t, u).apply(v), name, initialize, Unsafe.class.getClassLoader()); }

		public static Class<?> forNameNonnull(BiFunction<? super Throwable, ? super String, ? extends Function<? super Boolean, ? extends Class<?>>> callback, String name, boolean initialize) { return requireNonNull(forName(callback, name, initialize)); }

		public static Class<?> forName(String name, boolean initialize) { return maskToNonnull(forName((t, u) -> v -> { throw rejectArguments(t, u, v); }, name, initialize)); }

		@Nullable
		public static Class<?> forName(BiFunction<? super Throwable, ? super String, ? extends Class<?>> callback, String name) { return forName((t, u) -> v -> callback.apply(t, u), name, true); }

		public static Class<?> forNameNonnull(BiFunction<? super Throwable, ? super String, ? extends Class<?>> callback, String name) { return requireNonNull(forName(callback, name)); }

		public static Class<?> forName(String name) { return maskToNonnull(forName((t, u) -> { throw rejectArguments(t, u); }, name)); }


		@Nullable
		public static <T> T newInstance(BiFunction<? super Throwable, ? super Class<? extends T>, ? extends T> callback, Class<? extends T> clazz) { try { return clazz.newInstance(); } catch (Throwable t) { return callback.apply(t, clazz); } }

		public static <T> T newInstanceNonnull(BiFunction<? super Throwable, ? super Class<? extends T>, ? extends T> callback, Class<? extends T> clazz) { return requireNonNull(newInstance(callback, clazz)); }

		public static <T> T newInstance(Class<? extends T> clazz) { return maskToNonnull(newInstance((t, u) -> { throw rejectArguments(t, u); }, clazz)); }


		@Nullable
		public static Field getField(BiFunction<? super Throwable, ? super Class<?>, ? extends Function<? super String, ? extends Field>> callback, Class<?> clazz, String name) { try { return clazz.getField(name); } catch (Throwable t) { return callback.apply(t, clazz).apply(name); } }

		public static Field getFieldNonnull(BiFunction<? super Throwable, ? super Class<?>, ? extends Function<? super String, ? extends Field>> callback, Class<?> clazz, String name) { return requireNonNull(getField(callback, clazz, name)); }

		public static Field getField(Class<?> clazz, String name) { return maskToNonnull(getField((t, u) -> v -> { throw rejectArguments(t, u, v); }, clazz, name)); }

		@Nullable
		public static Field getDeclaredField(BiFunction<? super Throwable, ? super Class<?>, ? extends Function<? super String, ? extends Field>> callback, Class<?> clazz, String name) { try { return clazz.getDeclaredField(name); } catch (Throwable t) { return callback.apply(t, clazz).apply(name); } }

		public static Field getDeclaredFieldNonnull(BiFunction<? super Throwable, ? super Class<?>, ? extends Function<? super String, ? extends Field>> callback, Class<?> clazz, String name) { return requireNonNull(getDeclaredField(callback, clazz, name)); }

		public static Field getDeclaredField(Class<?> clazz, String name) { return maskToNonnull(getDeclaredField((t, u) -> v -> { throw rejectArguments(t, u, v); }, clazz, name)); }


		@Nullable
		public static AccessibleObjectAdapter.MethodAdapter getMethod(BiFunction<? super Throwable, ? super Class<?>, ? extends Function<? super String, ? extends AccessibleObjectAdapter.MethodAdapter>> callback, Class<?> clazz, String name) { try { return new AccessibleObjectAdapter.MethodAdapter(clazz.getMethod(name)); } catch (Throwable t) { return callback.apply(t, clazz).apply(name); } }

		public static AccessibleObjectAdapter.MethodAdapter getMethodNonnull(BiFunction<? super Throwable, ? super Class<?>, ? extends Function<? super String, ? extends AccessibleObjectAdapter.MethodAdapter>> callback, Class<?> clazz, String name) { return requireNonNull(getMethod(callback, clazz, name)); }

		public static AccessibleObjectAdapter.MethodAdapter getMethod(Class<?> clazz, String name) { return maskToNonnull(getMethod((t, u) -> v -> { throw rejectArguments(t, u, v); }, clazz, name)); }

		@Nullable
		public static AccessibleObjectAdapter.MethodAdapter getDeclaredMethod(BiFunction<? super Throwable, ? super Class<?>, ? extends Function<? super String, ? extends AccessibleObjectAdapter.MethodAdapter>> callback, Class<?> clazz, String name) { try { return new AccessibleObjectAdapter.MethodAdapter(clazz.getDeclaredMethod(name)); } catch (Throwable t) { return callback.apply(t, clazz).apply(name); } }

		public static AccessibleObjectAdapter.MethodAdapter getDeclaredMethodNonnull(BiFunction<? super Throwable, ? super Class<?>, ? extends Function<? super String, ? extends AccessibleObjectAdapter.MethodAdapter>> callback, Class<?> clazz, String name) { return requireNonNull(getDeclaredMethod(callback, clazz, name)); }

		public static AccessibleObjectAdapter.MethodAdapter getDeclaredMethod(Class<?> clazz, String name) { return maskToNonnull(getDeclaredMethod((t, u) -> v -> { throw rejectArguments(t, u, v); }, clazz, name)); }


		@Nullable
		public static <T> AccessibleObjectAdapter.ConstructorAdapter<T> getConstructor(BiFunction<? super Throwable, ? super Class<? extends T>, ? extends Function<? super Class<?>[], ? extends AccessibleObjectAdapter.ConstructorAdapter<T>>> callback, Class<? extends T> clazz, Class<?>... parameterTypes) { try { return new AccessibleObjectAdapter.ConstructorAdapter<>(clazz.getConstructor(parameterTypes)); } catch (Throwable t) { return callback.apply(t, clazz).apply(parameterTypes); } }

		public static <T> AccessibleObjectAdapter.ConstructorAdapter<T> getConstructorNonnull(BiFunction<? super Throwable, ? super Class<? extends T>, ? extends Function<? super Class<?>[], ? extends AccessibleObjectAdapter.ConstructorAdapter<T>>> callback, Class<? extends T> clazz, Class<?>... parameterTypes) { return requireNonNull(getConstructor(callback, clazz, parameterTypes)); }

		public static <T> AccessibleObjectAdapter.ConstructorAdapter<T> getConstructor(Class<? extends T> clazz, Class<?>... parameterTypes) { return maskToNonnull(getConstructor((t, u) -> v -> { throw rejectArguments(t, u, v); }, clazz, parameterTypes)); }

		@Nullable
		public static <T> AccessibleObjectAdapter.ConstructorAdapter<T> getDeclaredConstructor(BiFunction<? super Throwable, ? super Class<? extends T>, ? extends Function<? super Class<?>[], ? extends AccessibleObjectAdapter.ConstructorAdapter<T>>> callback, Class<? extends T> clazz, Class<?>... parameterTypes) { try { return new AccessibleObjectAdapter.ConstructorAdapter<>(clazz.getDeclaredConstructor(parameterTypes)); } catch (Throwable t) { return callback.apply(t, clazz).apply(parameterTypes); } }

		public static <T> AccessibleObjectAdapter.ConstructorAdapter<T> getDeclaredConstructorNonnull(BiFunction<? super Throwable, ? super Class<? extends T>, ? extends Function<? super Class<?>[], ? extends AccessibleObjectAdapter.ConstructorAdapter<T>>> callback, Class<? extends T> clazz, Class<?>... parameterTypes) { return requireNonNull(getDeclaredConstructor(callback, clazz, parameterTypes)); }

		public static <T> AccessibleObjectAdapter.ConstructorAdapter<T> getDeclaredConstructor(Class<? extends T> clazz, Class<?>... parameterTypes) { return maskToNonnull(getDeclaredConstructor((t, u) -> v -> { throw rejectArguments(t, u, v); }, clazz, parameterTypes)); }


		/* SECTION static classes */

		public static class AccessibleObjectAdapter<V extends AccessibleObject, T extends AccessibleObjectAdapter<V, T>> extends IAdapter.INonnull.Impl.Immutable<V, T> {
			/* SECTION constructors */

			public AccessibleObjectAdapter(V value) { super(value); }

			public AccessibleObjectAdapter(IAdapter.INonnull<? extends V, ?> copy) { this(copy.get()); }


			/* SECTION methods */

			public void setAccessible(BiFunction<? super Throwable, ? super T, ? extends Consumer<? super Boolean>> callback, boolean flag) { try { get().setAccessible(flag); } catch (Throwable t) { callback.apply(t, castUnchecked(this)).accept(flag); } }

			public void setAccessible(boolean flag) { setAccessible((t, u) -> v -> { throw rejectArguments(t, u, v); }, flag); }


			/* SECTION static classes */

			public static final class MethodAdapter extends AccessibleObjectAdapter<Method, MethodAdapter> {
				/* SECTION constructors */

				public MethodAdapter(Method value) { super(value); }

				public MethodAdapter(INonnull<? extends Method, ?> copy) { this(copy.get()); }


				/* SECTION methods */

				@Nullable
				public <O> Object invoke(BiFunction<? super Throwable, ? super MethodAdapter, ? extends BiFunction<? super O, ? super Object[], ?>> callback, @Nullable O obj, Object... args) { try { return get().invoke(obj, args); } catch (Throwable t) { return callback.apply(t, this).apply(obj, args); } }

				public <O> Object invokeNonnull(BiFunction<? super Throwable, ? super MethodAdapter, ? extends BiFunction<? super O, ? super Object[], ?>> callback, @Nullable O obj, Object... args) { return requireNonNull(invoke(callback, obj, args)); }

				@Nullable
				public Object invoke(@Nullable Object obj, Object... args) { return invoke((t, u) -> (v, w) -> { throw rejectArguments(t, u, v, w); }, obj, args); }

				public Object invokeNonnull(@Nullable Object obj, Object... args) { return requireNonNull(invoke(obj, args)); }
			}


			public static final class ConstructorAdapter<C> extends AccessibleObjectAdapter<Constructor<? extends C>, ConstructorAdapter<C>> {
				/* SECTION constructors */

				public ConstructorAdapter(Constructor<? extends C> value) { super(value); }

				public ConstructorAdapter(INonnull<? extends Constructor<? extends C>, ?> copy) { this(copy.get()); }


				/* SECTION methods */

				public C newInstance(BiFunction<? super Throwable, ? super ConstructorAdapter<C>, Function<? super Object[], ? extends C>> callback, Object... args) { try { return get().newInstance(args); } catch (Throwable t) { return callback.apply(t, this).apply(args); } }

				public C newInstance(Object... args) { return newInstance((t, u) -> v -> { throw rejectArguments(t, u, v); }, args); }
			}
		}
	}
}
