package $group__.$modId__.utilities.helpers;

import $group__.$modId__.annotations.runtime.ExternalCloneMethod;
import $group__.$modId__.utilities.extensions.ICloneable;
import $group__.$modId__.utilities.helpers.specific.Throwables;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
import static $group__.$modId__.utilities.helpers.specific.Loggers.EnumMessages.*;
import static $group__.$modId__.utilities.helpers.specific.Throwables.*;

public enum Copiers {
	/* MARK empty */;


	/* SECTION static variables */

	private static final Logger LOGGER = LogManager.getLogger(Copiers.class);


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
	public static <T> T copyEmpty(T copy) { return tryCall(() -> copy.getClass().newInstance(), LOGGER).<T>flatMap(Casts::castUnchecked).orElseThrow(Throwables::rethrowCaughtThrowableStatic); }


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
		Class<?> cl = copy.getClass();

		Constructor<?>[] co0s = cl.getDeclaredConstructors();
		HashMap<Constructor<C>, String> cos = new HashMap<>(co0s.length);
		for (Constructor<?> co0 : co0s) {
			Class<?>[] co0Args = co0.getParameterTypes();
			switch (co0Args.length) {
				case 1:
					Class<?> co0Arg1 = co0Args[0];
					if (Collection.class.isAssignableFrom(co0Arg1))
						cos.put(castUncheckedUnboxedNonnull(co0), "Copy");
					else if (Object[].class.isAssignableFrom(co0Arg1))
						cos.put(castUncheckedUnboxedNonnull(co0), "Object[]");
					break;
				default:
					break;
			}
		}

		@Nullable C r = null;
		cos:
		for (Map.Entry<Constructor<C>, String> coe : cos.entrySet()) {
			Constructor<C> co = coe.getKey();

			tryRun(() -> co.setAccessible(true), LOGGER);
			consumeIfCaughtThrowable(t -> LOGGER.warn(() -> SUFFIX_WITH_THROWABLE.makeMessage(REFLECTION_UNABLE_TO_SET_ACCESSIBLE.makeMessage("copy constructor", co, cl, true), t)));
			try {
				switch (coe.getValue()) {
					case "Copy":
						r =
								tryCall(() -> co.newInstance(copy), LOGGER).orElseThrow(Throwables::rethrowCaughtThrowableStatic);
						try {
							r.clear();
							r.addAll(copy.stream().map(o -> ICloneable.tryCloneUnboxed(o, LOGGER)).collect(Collectors.toList())); // COMMENT keep it ordered by collecting stream to list
						} catch (UnsupportedOperationException e) {
							// COMMENT copy is (probably) immutable
							consumeCaught(e, LOGGER);
							LOGGER.debug(() -> SUFFIX_WITH_THROWABLE.makeMessage(FACTORY_PARAMETERIZED_MESSAGE.makeMessage("Collection '{}' of class '{}' is (probably) immutable", copy, cl.toGenericString()), e));
						}
						break cos;
					case "Object[]":
						r = tryCall(() -> co.newInstance((Object) copy.stream().map(o -> ICloneable.tryCloneUnboxed(o,
								LOGGER)).toArray()), LOGGER).orElseThrow(Throwables::rethrowCaughtThrowableStatic); //
						// COMMENT cast to Object to call the method with the array as 1 object
						break cos;
				}
			} catch (Throwable t) {
				Throwables.setCaughtThrowableStatic(t, LOGGER);
			}
		}

		if (r == null) throw Throwables.rethrowCaughtThrowableStatic();
		return r;
	}


	// COMMENT miscellaneous

	@ExternalCloneMethod(ReentrantLock.class)
	public static ReentrantLock copyReentrantLock(ReentrantLock copy) { return new ReentrantLock(copy.isFair()); }

	@ExternalCloneMethod(ReentrantReadWriteLock.class)
	public static ReentrantReadWriteLock copyReentrantReadWriteLock(ReentrantReadWriteLock copy) { return new ReentrantReadWriteLock(copy.isFair()); }


	@ExternalCloneMethod(Color.class)
	public static Color copyColor(Color copy) { return new Color(copy.getColorSpace(), copy.getColorComponents(null),
			copy.getAlpha() / 255F); }

	@ExternalCloneMethod(ResourceLocation.class)
	public static ResourceLocation copyResourceLocation(ResourceLocation copy) { return new ResourceLocation(copy.getResourceDomain(), copy.getResourcePath()); }
}
