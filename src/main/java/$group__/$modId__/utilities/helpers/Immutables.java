package $group__.$modId__.utilities.helpers;

import $group__.$modId__.utilities.constructs.interfaces.annotations.ExternalToImmutableMethod;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.lang.reflect.Member;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@SuppressWarnings("SpellCheckingInspection")
public enum Immutables {
	/* MARK empty */;


	/* SECTION static methods */

	@ExternalToImmutableMethod({
			String.class,
			Color.class,
			ResourceLocation.class
	})
	public static Object toImmutableImmutable(Object mutable) { return mutable; }

	@ExternalToImmutableMethod(value = {
			Enum.class,
			Member.class
	}, allowExtends = true)
	public static Object toImmutableImmutableExtends(Object mutable) { return mutable; }


	@ExternalToImmutableMethod(value = Collection.class, allowExtends = true)
	public static <T> Collection<T> toImmutableCollection(Collection<? extends T> mutable) { return Collections.unmodifiableCollection(mutable); }

	@ExternalToImmutableMethod(value = List.class, allowExtends = true)
	public static <T> List<T> toImmutableList(List<? extends T> mutable) { return Collections.unmodifiableList(mutable); }

	@ExternalToImmutableMethod(value = Set.class, allowExtends = true)
	public static <T> Set<T> toImmutableSet(Set<? extends T> mutable) { return Collections.unmodifiableSet(mutable); }
}
