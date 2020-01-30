package $group__.$modId__.common.registrable.utilities.constructs;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface IForgeRegistryEntryExtension<V extends IForgeRegistryEntry<V>> extends IForgeRegistryEntry<V> {
	/* SECTION methods */

	@SuppressWarnings("SpellCheckingInspection")
	V setUnlocalizedName(ResourceLocation name);

	@SuppressWarnings("SpellCheckingInspection")
	default V setRegistryAndUnlocalizedName(String modId, String name) { return setRegistryAndUnlocalizedName(new ResourceLocation(modId, name)); }

	@SuppressWarnings("SpellCheckingInspection")
	V setRegistryAndUnlocalizedName(ResourceLocation name);
}
