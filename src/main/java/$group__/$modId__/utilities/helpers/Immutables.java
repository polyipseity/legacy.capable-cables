package $group__.$modId__.utilities.helpers;

import $group__.$modId__.annotations.ExternalToImmutableMethod;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.lang.reflect.Member;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Iterables.unmodifiableIterable;
import static java.util.Collections.unmodifiableCollection;

@SuppressWarnings("SpellCheckingInspection")
public enum Immutables {
	/* MARK empty */;


	/* SECTION static methods */

	// COMMENT simple

	@ExternalToImmutableMethod({
			String.class,
			Color.class,
			ResourceLocation.class
	})
	public static <T> T toImmutableImmutable(T mutable) { return mutable; }

	@ExternalToImmutableMethod(value = {
			Enum.class,
			Member.class
	}, allowExtends = true)
	public static <T> T toImmutableImmutableExtends(T mutable) { return mutable; }

	@ExternalToImmutableMethod(Object.class)
	public static <T> T toImmutableEmpty(T copy) { return copy; }


	@ExternalToImmutableMethod(value = {
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
	public static <T> T toImmutablePrimitives(T mutable) { return mutable; }


	// COMMENT iterables & collections

	@ExternalToImmutableMethod(value = Iterable.class, allowExtends = true)
	public static <T> Iterable<T> toImmutableIterableExtends(Iterable<? extends T> mutable) { return unmodifiableIterable(mutable); }

	@ExternalToImmutableMethod(value = Collection.class, allowExtends = true)
	public static <T> Collection<T> toImmutableCollectionExtends(Collection<? extends T> mutable) { return unmodifiableCollection(mutable); }

	@ExternalToImmutableMethod(value = List.class, allowExtends = true)
	public static <T> List<T> toImmutableListExtends(List<? extends T> mutable) { return ImmutableList.copyOf(mutable); }

	@ExternalToImmutableMethod(value = Set.class, allowExtends = true)
	public static <T> Set<T> toImmutableSetExtends(Set<? extends T> mutable) { return ImmutableSet.copyOf(mutable); }

	@ExternalToImmutableMethod(value = Map.class, allowExtends = true)
	public static <K, V> Map<K, V> toImmutableMapExtends(Map<? extends K, ? extends V> mutable) { return ImmutableMap.copyOf(mutable); }
}
