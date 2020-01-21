package $group__.$modId__.utilities.helpers;

import $group__.$modId__.client.gui.utilities.constructs.IThemed;
import $group__.$modId__.utilities.constructs.interfaces.annotations.ExternalToImmutableMethod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@SuppressWarnings("SpellCheckingInspection")
public enum Immutables {
	/* MARK empty */ ;


	/* SECTION static methods */

	@ExternalToImmutableMethod({
			Color.class,
			ResourceLocation.class
	})
	public static Object toImmutableImmutable(Object immutable) { return immutable; }

	@ExternalToImmutableMethod(value = {

	}, allowExtends = true)
	public static Object toImmutableImmutableExtends(Object copy) { return copy; }


	@ExternalToImmutableMethod(value = Collection.class, allowExtends = true)
	public static <T> Collection<T> toImmutableCollection(Collection<? extends T> mutable) { return Collections.unmodifiableCollection(mutable); }

	@ExternalToImmutableMethod(value = List.class, allowExtends = true)
	public static <T> List<T> toImmutableList(List<? extends T> mutable) { return Collections.unmodifiableList(mutable); }

	@ExternalToImmutableMethod(value = Set.class, allowExtends = true)
	public static <T> Set<T> toImmutableCollection(Set<? extends T> mutable) { return Collections.unmodifiableSet(mutable); }


	/* SECTION static classes */

	@SideOnly(Side.CLIENT)
	public enum Client {
		/* MARK empty */ ;


		/* SECTION static methods */

		@ExternalToImmutableMethod(IThemed.EnumTheme.class)
		public static Object toImmutableImmutable(Object immutable) { return immutable; }
	}
}
