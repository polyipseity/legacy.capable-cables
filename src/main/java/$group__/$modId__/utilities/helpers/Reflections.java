package $group__.$modId__.utilities.helpers;

import $group__.$modId__.utilities.constructs.interfaces.basic.IAdapter;
import $group__.$modId__.utilities.constructs.interfaces.basic.IThrowableCatcher;
import $group__.$modId__.utilities.helpers.Reflections.Unsafe.AccessibleObjectAdapter.ConstructorAdapter;
import $group__.$modId__.utilities.helpers.Reflections.Unsafe.AccessibleObjectAdapter.FieldAdapter;
import $group__.$modId__.utilities.helpers.Reflections.Unsafe.AccessibleObjectAdapter.MethodAdapter;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import sun.corba.Bridge;

import javax.annotation.Nullable;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.stream.IntStream;

import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Throwables.consumeCaught;
import static $group__.$modId__.utilities.variables.Globals.clearCaughtThrowableStatic;
import static $group__.$modId__.utilities.variables.Globals.setCaughtThrowableStatic;

public enum Reflections {
	/* MARK empty */;


	/* SECTION static variables */

	public static final Bridge BRIDGE = Bridge.get();


	/* SECTION static methods */

	public static boolean isClassAbstract(Class<?> clazz) { return clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers()); }

	public static boolean isMemberStatic(Member member) { return Modifier.isStatic(member.getModifiers()); }

	public static boolean isMemberFinal(Member member) { return Modifier.isFinal(member.getModifiers()); }


	public static String getPackage(Class<?> clazz) {
		String r = clazz.getName().replace(clazz.getSimpleName(), StringUtils.EMPTY);
		if (r.endsWith(".")) r = r.substring(0, r.length() - 1);
		return r;
	}


	public static <U> LinkedHashSet<Class<? extends U>> getIntermediateSuperclasses(Class<? extends U> lower, @Nullable Class<U> upper) {
		LinkedHashSet<Class<? extends U>> r = getLowerAndIntermediateSuperclasses(lower, upper);
		r.remove(lower);
		return r;
	}

	public static <U> LinkedHashSet<Class<? extends U>> getLowerAndIntermediateSuperclasses(Class<? extends U> lower, @Nullable Class<U> upper) {
		LinkedHashSet<Class<? extends U>> r = new LinkedHashSet<>();
		for (@Nullable Class<?> i = lower; i != upper && i != null; i = i.getSuperclass()) r.add(castUncheckedUnboxedNonnull(i));
		return r;
	}


	public static LinkedHashSet<LinkedHashSet<Class<?>>> getSuperclassesAndInterfaces(Class<?> c) {
		LinkedHashSet<LinkedHashSet<Class<?>>> r = new LinkedHashSet<>();
		@Nullable Class<?> cs = c.getSuperclass();
		LinkedHashSet<Class<?>> l = new LinkedHashSet<>(cs == null ? Arrays.asList(c.getInterfaces()) : Lists.asList(cs, c.getInterfaces()));
		while (!l.isEmpty()) {
			r.add(l);
			LinkedHashSet<Class<?>> l1 = new LinkedHashSet<>();
			l.forEach(t -> {
				@Nullable Class<?> ts = t.getSuperclass();
				l1.addAll(ts == null ? Arrays.asList(t.getInterfaces()) : Lists.asList(ts, t.getInterfaces()));
			});
			l = l1;
		}
		return r;
	}


	public static String getMethodNameDescriptor(Method m) { return m.getName() + org.objectweb.asm.Type.getMethodDescriptor(m); }


	public static boolean isFormerMethodOverriddenByLatter(Method a, Method b) {
		if (!(a.getName().equals(b.getName()) && a.getReturnType().isAssignableFrom(b.getReturnType()))) return false;
		Class<?>[] ap = a.getParameterTypes(), bp = b.getParameterTypes();
		return ap.length == bp.length && IntStream.range(0, ap.length).allMatch(i -> bp[i].isAssignableFrom(ap[i]));
	}

	public static Type getGenericSuperclassActualTypeArgument(Class<?> c, int i) throws ClassCastException { return getGenericSuperclassActualTypeArguments(c)[i]; }

	public static Type[] getGenericSuperclassActualTypeArguments(Class<?> c) { return ((ParameterizedType) c.getGenericSuperclass()).getActualTypeArguments(); }


	/* SECTION static classes */

	public enum Unsafe {
		/* MARK empty */;


		/* SECTION static methods */

		public static Optional<Class<?>> forName(String name) { return forName(name, true); }

		public static Optional<Class<?>> forName(String name, boolean initialize) { return forName(name, initialize, Unsafe.class.getClassLoader()); }

		public static Optional<Class<?>> forName(String name, boolean initialize, ClassLoader loader) {
			clearCaughtThrowableStatic();
			try {
				return Optional.of(Class.forName(name, initialize, loader));
			} catch (Throwable t) {
				setCaughtThrowableStatic(t);
				return Optional.empty();
			}
		}

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

		@SuppressWarnings("UnnecessaryFullyQualifiedName")
		public static class AccessibleObjectAdapter<V extends java.lang.reflect.AccessibleObject /* COMMENT error: cannot find symbol if qualification NOT used */, T extends AccessibleObjectAdapter<V, T>> extends IAdapter.IOptional.IImmutable.Impl<V, T> implements IThrowableCatcher {
			/* SECTION variables */

			protected final ThreadLocal<Throwable> caughtThrowable = new ThreadLocal<>();


			/* SECTION constructors */

			public AccessibleObjectAdapter(@Nullable V value) { super(value); }


			/* SECTION methods */

			@SuppressWarnings({"UnusedReturnValue"})
			public boolean setAccessible(boolean flag) {
				clearCaughtThrowable();
				final boolean[] r = {false};
				get().ifPresent(t -> {
					try {
						t.setAccessible(flag);
						r[0] = true;
					} catch (Throwable th) {
						consumeCaught(th);
						caughtThrowable.set(th);
					}
				});
				return r[0];
			}


			@Override
			public Optional<Throwable> getCaughtThrowable() { return Optional.ofNullable(caughtThrowable.get()); }

			@Override
			public void clearCaughtThrowable() { caughtThrowable.set(null); }


			/* SECTION static classes */

			public static final class FieldAdapter extends AccessibleObjectAdapter<Field, FieldAdapter> {
				/* SECTION static variables */

				private static final FieldAdapter EMPTY = new FieldAdapter(null);


				/* SECTION constructors */

				protected FieldAdapter(@Nullable Field value) { super(value); }


				/* SECTION static methods */

				public static FieldAdapter of(@Nullable Field value) { return value == null ? empty() : new FieldAdapter(value); }

				public static FieldAdapter empty() { return EMPTY; }


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

				public boolean set(@Nullable Object obj, Object value) {
					clearCaughtThrowable();
					final boolean[] r = {false};
					get().ifPresent(t -> {
						try {
							t.set(obj, value);
							r[0] = true;
						} catch (Throwable th) {
							consumeCaught(th);
							caughtThrowable.set(th);
						}
					});
					return r[0];
				}

				public boolean setInt(@Nullable Object obj, int i) {
					clearCaughtThrowable();
					final boolean[] r = {false};
					get().ifPresent(t -> {
						try {
							t.setInt(obj, i);
							r[0] = true;
						} catch (Throwable th) {
							consumeCaught(th);
							caughtThrowable.set(th);
						}
					});
					return r[0];
				}
			}


			public static final class MethodAdapter extends AccessibleObjectAdapter<Method, MethodAdapter> {
				/* SECTION static variables */

				private static final MethodAdapter EMPTY = new MethodAdapter(null);


				/* SECTION constructors */

				protected MethodAdapter(@Nullable Method value) { super(value); }


				/* SECTION static methods */

				public static MethodAdapter of(@Nullable Method value) { return value == null ? empty() : new MethodAdapter(value); }

				public static MethodAdapter empty() { return EMPTY; }


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
			}


			public static final class ConstructorAdapter<C> extends AccessibleObjectAdapter<Constructor<? extends C>, ConstructorAdapter<C>> {
				/* SECTION static variables */

				private static final ConstructorAdapter<?> EMPTY = new ConstructorAdapter<>((Constructor<?>) null);


				/* SECTION constructors */

				protected ConstructorAdapter(@Nullable Constructor<? extends C> value) { super(value); }


				/* SECTION static methods */

				public static <T> ConstructorAdapter<T> of(@Nullable Constructor<? extends T> value) { return value == null ? empty() : new ConstructorAdapter<>(value); }

				public static <T> ConstructorAdapter<T> empty() { return castUncheckedUnboxedNonnull(EMPTY); }


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
			}
		}
	}
}
