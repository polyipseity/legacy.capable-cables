package $group__.$modId__.utilities.helpers;

import $group__.$modId__.utilities.constructs.interfaces.basic.IAdapter;
import $group__.$modId__.utilities.helpers.Reflections.Classes.AccessibleObjectAdapter.ConstructorAdapter;
import $group__.$modId__.utilities.helpers.Reflections.Classes.AccessibleObjectAdapter.FieldAdapter;
import $group__.$modId__.utilities.helpers.Reflections.Classes.AccessibleObjectAdapter.MethodAdapter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import sun.corba.Bridge;

import javax.annotation.Nullable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;

import static $group__.$modId__.utilities.helpers.Casts.castChecked;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Loggers.*;
import static $group__.$modId__.utilities.helpers.Reflections.Classes.AccessibleObjectAdapter.FieldAdapter.setModifiers;
import static $group__.$modId__.utilities.helpers.Reflections.Classes.AccessibleObjectAdapter.FieldAdapter.setWithLogging;
import static $group__.$modId__.utilities.helpers.Reflections.Classes.AccessibleObjectAdapter.setAccessibleWithLogging;
import static $group__.$modId__.utilities.helpers.Reflections.Classes.getDeclaredMethod;
import static $group__.$modId__.utilities.helpers.Throwables.*;
import static $group__.$modId__.utilities.variables.Globals.*;
import static java.lang.invoke.LambdaMetafactory.metafactory;
import static java.lang.invoke.MethodType.methodType;

public enum Reflections {
	/* MARK empty */;


	/* SECTION static variables */

	public static final Lookup PUBLIC_LOOKUP = MethodHandles.publicLookup();
	public static final Lookup IMPL_LOOKUP;
	public static final Bridge BRIDGE = Bridge.get();

	public static final MethodAdapter METHOD_OBJECT_TO_STRING;
	public static final Method METHOD_OBJECT_TO_STRING0;
	public static final MethodAdapter METHOD_OBJECT_CLONE;
	public static final Method METHOD_OBJECT_CLONE0;


	/* SECTION static methods */

	public static boolean isClassAbstract(Class<?> clazz) { return clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers()); }

	public static boolean isMemberStatic(Member member) { return Modifier.isStatic(member.getModifiers()); }

	public static boolean isMemberFinal(Member member) { return Modifier.isFinal(member.getModifiers()); }


	public static String getPackage(Class<?> clazz) {
		String r = clazz.getName().replace(clazz.getSimpleName(), StringsExtension.EMPTY);
		if (r.endsWith(".")) r = r.substring(0, r.length() - 1);
		return r;
	}


	public static Class<?>[] getClassStackTrace() {
		Class<?>[] r = SecurityManagerReflections.INSTANCE.getClassContext();
		return Arrays.copyOfRange(r, 2, r.length);
	}

	public static Class<?> getClassStackTrace(int depth) { return getClassStackTrace()[1 + depth]; }

	public static Class<?> getCurrentClass() { return getClassStackTrace(1); }

	public static Class<?> getCallerClass() { return getClassStackTrace(2); }


	public static <U> LinkedHashSet<Class<? extends U>> getIntermediateSuperclasses(Class<? extends U> lower, @Nullable Class<U> upper) {
		LinkedHashSet<Class<? extends U>> r = getLowerAndIntermediateSuperclasses(lower, upper);
		r.remove(lower);
		return r;
	}

	public static <U> LinkedHashSet<Class<? extends U>> getSuperclasses(Class<? extends U> lower) { return getIntermediateSuperclasses(lower, null); }

	public static <U> LinkedHashSet<Class<? extends U>> getLowerAndIntermediateSuperclasses(Class<? extends U> lower, @Nullable Class<U> upper) {
		LinkedHashSet<Class<? extends U>> r = new LinkedHashSet<>();
		for (@Nullable Class<?> i = lower; i != upper && i != null; i = i.getSuperclass()) r.add(castUncheckedUnboxedNonnull(i));
		return r;
	}

	public static <U> LinkedHashSet<Class<? extends U>> getThisAndSuperclasses(Class<? extends U> lower) { return getLowerAndIntermediateSuperclasses(lower, null); }


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


	/* SECTION static initializer */

	static {
		Lookup IMPL_LOOKUP1 = PUBLIC_LOOKUP;
		for (Field f : Lookup.class.getDeclaredFields()) {
			if ("IMPL_LOOKUP".equals(f.getName())) {
				f.setAccessible(true);
				final Lookup[] implLookup = new Lookup[1];
				tryCallWithLogging(() -> implLookup[0] = (Lookup) PUBLIC_LOOKUP.unreflectGetter(f).invokeExact(), LOGGER);
				if (implLookup[0] != null) IMPL_LOOKUP1 = implLookup[0];
				break;
			}
		}
		IMPL_LOOKUP = IMPL_LOOKUP1;

		METHOD_OBJECT_TO_STRING0 = (METHOD_OBJECT_TO_STRING = getDeclaredMethod(Object.class, "toString")).get().orElseThrow(Throwables::unexpected);
		METHOD_OBJECT_CLONE0 = (METHOD_OBJECT_CLONE = getDeclaredMethod(Object.class, "clone")).get().orElseThrow(Throwables::unexpected);
	}


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

	public enum MethodHandlesExtension {
		/* MARK empty */;


		/* SECTION static methods */

		@SuppressWarnings("SpellCheckingInspection")
		public enum Lookups {
			/* MARK empty */;


			/* SECTION static methods */

			public static Optional<MethodHandle> findStatic(Lookup lookup, Class<?> refc, String name, MethodType type) {
				clearCaughtThrowableStatic();
				try {
					return Optional.of(lookup.findStatic(refc, name, type));
				} catch (Throwable t) {
					setCaughtThrowableStatic(t);
					return Optional.empty();
				}
			}

			public static Optional<MethodHandle> findVirtual(Lookup lookup, Class<?> refc, String name, MethodType type) {
				clearCaughtThrowableStatic();
				try {
					return Optional.of(lookup.findVirtual(refc, name, type));
				} catch (Throwable t) {
					setCaughtThrowableStatic(t);
					return Optional.empty();
				}
			}


		}
	}

	public enum Classes {
		/* MARK empty */;


		/* SECTION static methods */

		public static Optional<Class<?>> forName(String name) { return forName(name, true); }

		public static Optional<Class<?>> forName(String name, boolean initialize) { return forName(name, initialize, Classes.class.getClassLoader()); }

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

		public enum Bulk {
			/* MARK empty */;


			/* SECTION static methods */

			public static <T> boolean mapFields(Class<T> common, T from, T to, Function<Object, ?> mapper, boolean supers) {
				final boolean[] r = {true};
				(supers ? getThisAndSuperclasses(common) : ImmutableList.of(common)).forEach(c -> {
					for (Field f : c.getDeclaredFields()) {
						if (isMemberStatic(f)) continue;
						FieldAdapter fa = FieldAdapter.of(f);
						setAccessibleWithLogging(fa, "to-be-copied field", from, c, true);

						@Nullable Object v = fa.get(from).orElse(null);
						if (caughtThrowableStatic()) {
							LOGGER.warn(FORMATTER_WITH_THROWABLE.apply(FORMATTER_REFLECTION_UNABLE_TO_GET_FIELD.apply(f, from).apply(c), getCaughtThrowableUnboxedNonnullStatic()));
							r[0] = false;
						} else {
							setModifiers(fa, f.getModifiers() & ~Modifier.FINAL);
							r[0] &= setWithLogging(fa, fa1 -> fa1::set, to, mapper.apply(v), c);
						}
					}
				});
				return r[0];
			}

			public static <T> boolean mapFields(Class<T> common, T from, T to, Function<Object, ?> mapper) {return mapFields(common, from, to, mapper, true);}

			public static <T> boolean copyFields(Class<T> common, T from, T to, boolean supers) { return mapFields(common, from, to, Function.identity(), supers); }

			@SuppressWarnings("UnusedReturnValue")
			public static <T> boolean copyFields(Class<T> common, T from, T to) {return copyFields(common, from, to, true);}
		}


		@SuppressWarnings("UnnecessaryFullyQualifiedName")
		public static class AccessibleObjectAdapter<V extends java.lang.reflect.AccessibleObject /* COMMENT error: cannot find symbol if qualification NOT used */, T extends AccessibleObjectAdapter<V, T>> extends IAdapter.IOptional.IImmutable.Impl<V, T> {
			/* SECTION constructors */

			public AccessibleObjectAdapter(@Nullable V value) { super(value); }


			/* SECTION static methods */

			@SuppressWarnings("UnusedReturnValue")
			public static boolean setAccessibleWithLogging(AccessibleObjectAdapter<?, ?> aoa, String desc, @Nullable Object obj, @Nullable Class<?> clazz, boolean flag) {
				boolean r;
				if (!(r = aoa.setAccessible(flag))) {
					AccessibleObject aoa0 = aoa.get().orElseThrow(() -> rejectArguments(aoa, desc, obj, flag));
					Optional<Member> aoa0m = castChecked(aoa0, Member.class);
					LOGGER.warn(FORMATTER_WITH_THROWABLE.apply(FORMATTER_REFLECTION_UNABLE_TO_SET_ACCESSIBLE.apply(() -> desc, aoa0).apply(aoa0m.map(Reflections::isMemberStatic).orElse(true) ? null : obj, clazz == null ? aoa0m.map(Member::getDeclaringClass).orElse(null) : clazz).apply(flag), getCaughtThrowableUnboxedNonnullStatic()));
				}
				return r;
			}


			/* SECTION methods */

			@SuppressWarnings({"UnusedReturnValue"})
			public boolean setAccessible(boolean flag) {
				clearCaughtThrowableStatic();
				final boolean[] r = {false};
				get().ifPresent(t -> {
					try {
						t.setAccessible(flag);
						r[0] = true;
					} catch (Throwable th) {
						setCaughtThrowableStatic(th);
					}
				});
				return r[0];
			}


			/* SECTION static classes */

			@SuppressWarnings("UnnecessaryFullyQualifiedName")
			public static final class FieldAdapter extends AccessibleObjectAdapter<java.lang.reflect.Field /* COMMENT error: cannot find symbol if qualification NOT used */, FieldAdapter> {
				/* SECTION static variables */

				private static final FieldAdapter EMPTY = new FieldAdapter(null);
				public static final FieldAdapter MODIFIERS = getDeclaredField(Field.class, "modifiers");
				@Nullable
				public static final Field MODIFIERS0 = MODIFIERS.get().orElseGet(() -> {
					LOGGER.warn("Modifiers field access failed", getCaughtThrowableUnboxedStatic());
					return null;
				});


				/* SECTION constructors */

				protected FieldAdapter(@Nullable Field value) { super(value); }


				/* SECTION static methods */

				public static FieldAdapter of(@Nullable Field value) { return value == null ? empty() : new FieldAdapter(value); }

				public static FieldAdapter empty() { return EMPTY; }


				public static <F extends FieldAdapter, T, O> Optional<T> getWithLogging(F f, BiFunction<? super F, ? super O, ? extends Optional<T>> getter, @Nullable O obj, @Nullable Class<?> clazz) {
					Optional<T> r = getter.apply(f, obj);
					if (caughtThrowableStatic()) {
						Field f0 = f.get().orElseThrow(() -> rejectArguments(f, obj, clazz));
						LOGGER.warn(FORMATTER_WITH_THROWABLE.apply(FORMATTER_REFLECTION_UNABLE_TO_GET_FIELD.apply(f0, obj).apply(clazz == null ? f0.getDeclaringClass() : clazz), getCaughtThrowableUnboxedNonnullStatic()));
					}
					return r;
				}

				public static <F extends FieldAdapter, O, V> boolean setWithLogging(F f, Function<? super F, BiFunction<? super O, ? super V, ? extends Boolean>> setter, @Nullable O obj, @Nullable V value, @Nullable Class<?> clazz) {
					boolean r = setter.apply(f).apply(obj, value);
					if (caughtThrowableStatic()) {
						Field f0 = f.get().orElseThrow(() -> rejectArguments(f, obj, clazz));
						LOGGER.warn(FORMATTER_WITH_THROWABLE.apply(FORMATTER_REFLECTION_UNABLE_TO_SET_FIELD.apply(f0, obj).apply(clazz == null ? f0.getDeclaringClass() : clazz, value), getCaughtThrowableUnboxedNonnullStatic()));
					}
					return r;
				}


				@SuppressWarnings("UnusedReturnValue")
				public static boolean setModifiers(FieldAdapter f, int mod) {
					if (MODIFIERS0 == null) return false;
					@Nullable Field f0 = f.get().orElse(null);
					if (f0 == null) return false;
					setAccessibleWithLogging(MODIFIERS, "modifiers field", f0, f0.getClass(), true);
					boolean r;
					if (!(r = MODIFIERS.setInt(f0, mod)))
						LOGGER.warn(FORMATTER_WITH_THROWABLE.apply(FORMATTER_REFLECTION_UNABLE_TO_SET_FIELD.apply(MODIFIERS0, f0).apply(Field.class, mod), getCaughtThrowableUnboxedNonnullStatic()));
					return r;
				}


				/* SECTION methods */

				public Optional<?> get(@Nullable Object obj) {
					clearCaughtThrowableStatic();
					return get().map(t -> {
						try { return t.get(obj); } catch (Throwable th) {
							setCaughtThrowableStatic(th);
							return null;
						}
					});
				}

				public Optional<Boolean> getBoolean(@Nullable Object obj) {
					clearCaughtThrowableStatic();
					return get().map(t -> {
						try { return t.getBoolean(obj); } catch (Throwable th) {
							setCaughtThrowableStatic(th);
							return null;
						}
					});
				}

				public Optional<Integer> getInt(@Nullable Object obj) {
					clearCaughtThrowableStatic();
					return get().map(t -> {
						try { return t.getInt(obj); } catch (Throwable th) {
							setCaughtThrowableStatic(th);
							return null;
						}
					});
				}

				public Optional<Long> getLong(@Nullable Object obj) {
					clearCaughtThrowableStatic();
					return get().map(t -> {
						try { return t.getLong(obj); } catch (Throwable th) {
							setCaughtThrowableStatic(th);
							return null;
						}
					});
				}

				public boolean set(@Nullable Object obj, @Nullable Object value) {
					clearCaughtThrowableStatic();
					final boolean[] r = {false};
					get().ifPresent(t -> {
						try {
							t.set(obj, value);
							r[0] = true;
						} catch (Throwable th) {
							setCaughtThrowableStatic(th);
						}
					});
					return r[0];
				}

				public boolean setBoolean(@Nullable Object obj, boolean z) {
					clearCaughtThrowableStatic();
					final boolean[] r = {false};
					get().ifPresent(t -> {
						try {
							t.setBoolean(obj, z);
							r[0] = true;
						} catch (Throwable th) {
							setCaughtThrowableStatic(th);
						}
					});
					return r[0];
				}

				public boolean setInt(@Nullable Object obj, int i) {
					clearCaughtThrowableStatic();
					final boolean[] r = {false};
					get().ifPresent(t -> {
						try {
							t.setInt(obj, i);
							r[0] = true;
						} catch (Throwable th) {
							setCaughtThrowableStatic(th);
						}
					});
					return r[0];
				}

				public boolean setLong(@Nullable Object obj, long l) {
					clearCaughtThrowableStatic();
					final boolean[] r = {false};
					get().ifPresent(t -> {
						try {
							t.setLong(obj, l);
							r[0] = true;
						} catch (Throwable th) {
							setCaughtThrowableStatic(th);
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
					clearCaughtThrowableStatic();
					return get().map(t -> {
						try { return t.invoke(obj, args); } catch (Throwable th) {
							setCaughtThrowableStatic(th);
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
					clearCaughtThrowableStatic();
					return get().map(t -> {
						try { return t.newInstance(args); } catch (Throwable th) {
							setCaughtThrowableStatic(th);
							return null;
						}
					});
				}
			}
		}
	}


	private static final class SecurityManagerReflections extends SecurityManager {
		/* SECTION static variables */
		
		private static final SecurityManagerReflections INSTANCE = new SecurityManagerReflections();
		
		
		/* SECTION constructors */

		private SecurityManagerReflections() { requireRunOnceOnly(); }


		/* SECTION methods */

		@SuppressWarnings("EmptyMethod")
		@Override
		protected Class<?>[] getClassContext() { return super.getClassContext(); }
	}
}
