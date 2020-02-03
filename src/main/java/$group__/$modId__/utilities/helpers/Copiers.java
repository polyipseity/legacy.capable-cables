package $group__.$modId__.utilities.helpers;

import $group__.$modId__.utilities.constructs.interfaces.annotations.ExternalCloneMethod;
import $group__.$modId__.utilities.constructs.interfaces.extensions.ICloneable;
import $group__.$modId__.utilities.helpers.Reflections.Classes.AccessibleObjectAdapter.ConstructorAdapter;
import $group__.$modId__.utilities.variables.Globals;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.*;
import java.util.stream.Collectors;

import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Reflections.Classes.AccessibleObjectAdapter.setAccessibleWithLogging;
import static $group__.$modId__.utilities.helpers.Reflections.Classes.newInstance;
import static $group__.$modId__.utilities.helpers.Throwables.consumeCaught;
import static $group__.$modId__.utilities.variables.Globals.*;

public enum Copiers {
	/* MARK empty */;


	/* SECTION static methods */

	// COMMENT simple

	@ExternalCloneMethod(value = {
			Enum.class,
			Callable.class,
			Consumer.class,
			BiConsumer.class,
			Function.class,
			BiFunction.class,
			Supplier.class,
			Member.class
	}, allowExtends = true)
	public static <T> T copySingletonExtends(T copy) { return copy; }

	@ExternalCloneMethod(Object.class)
	public static <T> T copyEmpty(T copy) { return newInstance(copy.getClass()).<T>flatMap(Casts::castUnchecked).orElseThrow(Globals::rethrowCaughtThrowableStatic); }


	@ExternalCloneMethod(value = {
			int.class, Integer.class,
			float.class, Float.class,
			double.class, Double.class,
			long.class, Long.class,
			byte.class, Byte.class,
			short.class, Short.class,
			boolean.class, Boolean.class,
			char.class, Character.class,
			void.class, Void.class // COMMENT improbable
	})
	public static <T> T copyPrimitives(T copy) { return copy; }


	// COMMENT collections

	@SuppressWarnings("SwitchStatementWithTooFewBranches")
	@ExternalCloneMethod(value = Collection.class, allowExtends = true)
	public static <C extends Collection<E>, E> C copyCollectionExtends(C copy) {
		Class<? extends C> cl = castUncheckedUnboxedNonnull(copy.getClass());

		Constructor<?>[] co0s = cl.getDeclaredConstructors();
		HashMap<ConstructorAdapter<? extends C>, String> cos = new HashMap<>(co0s.length);
		for (Constructor<?> co0 : co0s) {
			Class<?>[] co0Args = co0.getParameterTypes();
			switch (co0Args.length) {
				case 1:
					Class<?> co0Arg1 = co0Args[0];
					if (Collection.class.isAssignableFrom(co0Arg1))
						cos.put(ConstructorAdapter.of(castUncheckedUnboxedNonnull(co0)), "Copy");
					else if (Object[].class.isAssignableFrom(co0Arg1))
						cos.put(ConstructorAdapter.of(castUncheckedUnboxedNonnull(co0)), "Object[]");
					break;
				default:
					break;
			}
		}

		@Nullable C r = null;
		cos:
		for (Map.Entry<ConstructorAdapter<? extends C>, String> co : cos.entrySet()) {
			ConstructorAdapter<? extends C> coa = co.getKey();

			setAccessibleWithLogging(coa, "copy constructor", null, cl, true);
			try {
				switch (co.getValue()) {
					case "Copy":
						r = coa.newInstance(copy).orElseThrow(Globals::rethrowCaughtThrowableStatic);
						try {
							r.clear();
							r.addAll(copy.stream().map(ICloneable::tryCloneUnboxed).collect(Collectors.toList())); // COMMENT keep it ordered by collecting stream to list
						} catch (UnsupportedOperationException e) {
							// COMMENT copy is (probably) immutable
							consumeCaught(e);
							LOGGER.debug("Collection '{}' of class '{}' is (probably) immutable", copy, cl.toGenericString());
						}
						break cos;
					case "Object[]":
						r = coa.newInstance((Object) copy.stream().map(ICloneable::tryCloneUnboxed).toArray()).orElseThrow(Globals::rethrowCaughtThrowableStatic); // COMMENT cast to Object to call the method with the array as 1 object
						break cos;
				}
			} catch (Throwable t) {
				setCaughtThrowableStatic(t);
			}
		}

		if (r == null) throw rethrowCaughtThrowableStatic();
		return r;
	}


	// COMMENT miscellaneous

	@ExternalCloneMethod(ReentrantLock.class)
	public static ReentrantLock copyReentrantLock(ReentrantLock copy) { return new ReentrantLock(copy.isFair()); }

	@ExternalCloneMethod(ReentrantReadWriteLock.class)
	public static ReentrantReadWriteLock copyReentrantReadWriteLock(ReentrantReadWriteLock copy) { return new ReentrantReadWriteLock(copy.isFair()); }


	@ExternalCloneMethod(Color.class)
	public static Color copyColor(Color copy) { return new Color(copy.getColorSpace(), copy.getColorComponents(null), copy.getAlpha() / 255F); }

	@ExternalCloneMethod(ResourceLocation.class)
	public static ResourceLocation copyResourceLocation(ResourceLocation copy) { return new ResourceLocation(copy.getResourceDomain(), copy.getResourcePath()); }
}
