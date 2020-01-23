package $group__.$modId__.utilities.helpers;

import $group__.$modId__.utilities.constructs.interfaces.annotations.ExternalCloneMethod;
import $group__.$modId__.utilities.constructs.interfaces.extensions.ICloneable;
import $group__.$modId__.utilities.helpers.Reflections.Unsafe.AccessibleObjectAdapter.ConstructorAdapter;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.*;
import java.util.stream.Collectors;

import static $group__.$modId__.utilities.helpers.Casts.castUnchecked;
import static $group__.$modId__.utilities.helpers.Reflections.Unsafe.getDeclaredConstructor;
import static $group__.$modId__.utilities.helpers.Reflections.Unsafe.getDeclaredConstructorNonnull;

public enum Copiers {
	/* MARK empty */ ;


	/* SECTION static methods */

	@ExternalCloneMethod(value = {
			Enum.class,
			Callable.class,
			Consumer.class,
			BiConsumer.class,
			Function.class,
			BiFunction.class,
			Supplier.class
	}, allowExtends = true)
	public static Object copySingletonExtends(Object copy) { return copy; }


	@ExternalCloneMethod(Color.class)
	public static Color copyColor(Color copy) { return new Color(copy.getColorSpace(), copy.getColorComponents(null), copy.getAlpha() / 255F); }

	@ExternalCloneMethod(ResourceLocation.class)
	public static ResourceLocation copyResourceLocation(ResourceLocation copy) { return new ResourceLocation(copy.getResourceDomain(), copy.getResourcePath()); }

	@ExternalCloneMethod(value = Collection.class, allowExtends = true)
	public static <C extends Collection<E>, E> C copyCollection(C copy) {
		Class<? extends C> cl = castUnchecked(copy.getClass());
		AtomicReference<Class<Object[]>> a = new AtomicReference<>(Object[].class);
		ConstructorAdapter<? extends C> co = getDeclaredConstructorNonnull((t, u) -> v -> getDeclaredConstructor(u, a.getAndSet(null)), cl, cl);

		return co.newInstance(a.get() == null ? copy.stream().map(ICloneable::tryClone).toArray() : copy.stream().map(ICloneable::tryClone).collect(Collectors.toList()));
	}
}
