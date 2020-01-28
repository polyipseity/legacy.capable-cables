package $group__.$modId__.utilities.helpers;

import $group__.$modId__.utilities.constructs.interfaces.basic.IAdapter;
import $group__.$modId__.utilities.constructs.interfaces.basic.IThrowableCatcher;
import $group__.$modId__.utilities.helpers.Reflections.Unsafe.AccessibleObjectAdapter.ConstructorAdapter;
import $group__.$modId__.utilities.helpers.Reflections.Unsafe.AccessibleObjectAdapter.FieldAdapter;
import $group__.$modId__.utilities.helpers.Reflections.Unsafe.AccessibleObjectAdapter.MethodAdapter;
import com.google.common.collect.Lists;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;

import javax.annotation.Nullable;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Throwables.consumeCaught;
import static $group__.$modId__.utilities.variables.Globals.clearCaughtThrowableStatic;
import static $group__.$modId__.utilities.variables.Globals.setCaughtThrowableStatic;
import static java.util.Objects.requireNonNull;

public enum Reflections {
	/* MARK empty */;


	/* SECTION static methods */

	public static boolean isClassAbstract(Class<?> c) { return c.isInterface() || Modifier.isAbstract(c.getModifiers()); }

	public static boolean isMemberStatic(Member m) { return Modifier.isStatic(m.getModifiers()); }

	public static boolean isMemberFinal(Member m) { return Modifier.isFinal(m.getModifiers()); }


	public static SetMultimap<Long, Class<?>> getSuperclassesAndInterfaces(Class<?> c) {
		@SuppressWarnings("UnstableApiUsage") SetMultimap<Long, Class<?>> r = MultimapBuilder.hashKeys().hashSetValues().build();
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

	public static Type getGenericSuperclassActualTypeArgument(Class<?> c, int i) throws ClassCastException { return getGenericSuperclassActualTypeArguments(c)[i]; }


	/* SECTION static classes */

	public enum Unsafe {
		/* MARK empty */;


		/* SECTION static methods */

		public static Optional<Class<?>> forName(String name, boolean initialize, ClassLoader loader) {
			clearCaughtThrowableStatic();
			try {
				return Optional.of(Class.forName(name, initialize, loader));
			} catch (Throwable t) {
				setCaughtThrowableStatic(t);
				return Optional.empty();
			}
		}

		public static Optional<Class<?>> forName(String name, boolean initialize) { return forName(name, initialize, Unsafe.class.getClassLoader()); }

		public static Optional<Class<?>> forName(String name) { return forName(name, true); }


		public static <T> Optional<T> newInstance(Class<? extends T> clazz) {
			clearCaughtThrowableStatic();
			try {
				return Optional.of(clazz.newInstance());
			} catch (Throwable t) {
				setCaughtThrowableStatic(t);
				return Optional.empty();
			}
		}


		public static FieldAdapter getField(Class<?> clazz, String name) {
			clearCaughtThrowableStatic();
			try {
				return FieldAdapter.of(clazz.getField(name));
			} catch (Throwable t) {
				setCaughtThrowableStatic(t);
				return FieldAdapter.empty();
			}
		}

		public static FieldAdapter getDeclaredField(Class<?> clazz, String name) {
			clearCaughtThrowableStatic();
			try {
				return FieldAdapter.of(clazz.getDeclaredField(name));
			} catch (Throwable t) {
				setCaughtThrowableStatic(t);
				return FieldAdapter.empty();
			}
		}


		public static MethodAdapter getMethod(Class<?> clazz, String name, Class<?>... args) {
			clearCaughtThrowableStatic();
			try {
				return MethodAdapter.of(clazz.getMethod(name, args));
			} catch (Throwable t) {
				setCaughtThrowableStatic(t);
				return MethodAdapter.empty();
			}
		}

		public static MethodAdapter getDeclaredMethod(Class<?> clazz, String name, Class<?>... args) {
			clearCaughtThrowableStatic();
			try {
				return MethodAdapter.of(clazz.getDeclaredMethod(name, args));
			} catch (Throwable t) {
				setCaughtThrowableStatic(t);
				return MethodAdapter.empty();
			}
		}


		public static <T> ConstructorAdapter<T> getConstructor(Class<? extends T> clazz, Class<?>... args) {
			clearCaughtThrowableStatic();
			try {
				return ConstructorAdapter.of(clazz.getConstructor(args));
			} catch (Throwable t) {
				setCaughtThrowableStatic(t);
				return ConstructorAdapter.empty();
			}
		}

		public static <T> ConstructorAdapter<T> getDeclaredConstructor(Class<? extends T> clazz, Class<?>... args) {
			clearCaughtThrowableStatic();
			try {
				return ConstructorAdapter.of(clazz.getDeclaredConstructor(args));
			} catch (Throwable t) {
				setCaughtThrowableStatic(t);
				return ConstructorAdapter.empty();
			}
		}


		/* SECTION static classes */

		public static class AccessibleObjectAdapter<V extends java.lang.reflect.AccessibleObject /* COMMENT error: cannot find symbol if qualification NOT used */, T extends AccessibleObjectAdapter<V, T>> extends IAdapter.IOptional.Impl.Immutable<V, T> implements IThrowableCatcher {
			/* SECTION variables */

			protected final ThreadLocal<Throwable> caughtThrowable = new ThreadLocal<>();


			/* SECTION constructors */

			public AccessibleObjectAdapter(@Nullable V value) { super(value); }


			/* SECTION methods */

			@SuppressWarnings({"UnusedReturnValue", "BooleanMethodIsAlwaysInverted"})
			public boolean setAccessible(boolean flag) {
				clearCaughtThrowable();
				try {
					final boolean[] r = {false};
					get().ifPresent(t -> {
						t.setAccessible(flag);
						r[0] = true;
					});
					return r[0];
				} catch (Throwable t) {
					consumeCaught(t);
					caughtThrowable.set(t);
					return false;
				}
			}


			@Override
			public Optional<Throwable> getCaughtThrowable() { return Optional.ofNullable(caughtThrowable.get()); }

			@Override
			public void clearCaughtThrowable() { caughtThrowable.set(null); }


			/* SECTION static classes */

			public static final class FieldAdapter extends AccessibleObjectAdapter<Field, FieldAdapter> {
				/* SECTION constructors */

				protected FieldAdapter(@Nullable Field value) { super(value); }

				public FieldAdapter(IAdapter.IOptional<? extends Field, ?> copy) { this(requireNonNull(copy.getUnboxed())); }

				public FieldAdapter(IAdapter<? extends Field, ?> copy) { this(requireNonNull(copy.get())); }


				/* SECTION methods */

				public Optional<?> get_(@Nullable Object obj) {
					clearCaughtThrowable();
					return get().map(t -> {
						try { return t.get(obj); } catch (Throwable th) {
							consumeCaught(th);
							caughtThrowable.set(th);
							return null;
						}
					});
				}


				/* SECTION static variables */

				private static final FieldAdapter EMPTY = new FieldAdapter((Field) null);


				/* SECTION static methods */

				public static FieldAdapter empty() { return EMPTY; }

				public static FieldAdapter of(@Nullable Field value) { return value == null ? empty() : new FieldAdapter(value); }
			}


			public static final class MethodAdapter extends AccessibleObjectAdapter<Method, MethodAdapter> {
				/* SECTION constructors */

				protected MethodAdapter(@Nullable Method value) { super(value); }

				public MethodAdapter(IAdapter.IOptional<? extends Method, ?> copy) { this(requireNonNull(copy.getUnboxed())); }

				public MethodAdapter(IAdapter<? extends Method, ?> copy) { this(requireNonNull(copy.get())); }


				/* SECTION methods */

				public Optional<?> invoke(@Nullable Object obj, Object... args) {
					clearCaughtThrowable();
					return get().map(t -> {
						try { return t.invoke(obj, args); } catch (Throwable th) {
							consumeCaught(th);
							caughtThrowable.set(th);
							return null;
						}
					});
				}


				/* SECTION static variables */

				private static final MethodAdapter EMPTY = new MethodAdapter((Method) null);


				/* SECTION static methods */

				public static MethodAdapter empty() { return EMPTY; }

				public static MethodAdapter of(@Nullable Method value) { return value == null ? empty() : new MethodAdapter(value); }
			}


			public static final class ConstructorAdapter<C> extends AccessibleObjectAdapter<Constructor<? extends C>, ConstructorAdapter<C>> {
				/* SECTION constructors */

				protected ConstructorAdapter(@Nullable Constructor<? extends C> value) { super(value); }

				public ConstructorAdapter(IAdapter.IOptional<? extends Constructor<? extends C>, ?> copy) { this(requireNonNull(copy.getUnboxed())); }

				public ConstructorAdapter(IAdapter<? extends Constructor<? extends C>, ?> copy) { this(requireNonNull(copy.get())); }


				/* SECTION methods */

				public Optional<C> newInstance(Object... args) {
					clearCaughtThrowable();
					return get().map(t -> {
						try { return t.newInstance(args); } catch (Throwable th) {
							consumeCaught(th);
							caughtThrowable.set(th);
							return null;
						}
					});
				}


				/* SECTION static variables */

				private static final ConstructorAdapter<?> EMPTY = new ConstructorAdapter<>((Constructor<?>) null);


				/* SECTION static methods */

				public static <T> ConstructorAdapter<T> empty() { return castUncheckedUnboxedNonnull(EMPTY); }

				public static <T> ConstructorAdapter<T> of(@Nullable Constructor<? extends T> value) { return value == null ? empty() : new ConstructorAdapter<>(value); }
			}
		}
	}
}
