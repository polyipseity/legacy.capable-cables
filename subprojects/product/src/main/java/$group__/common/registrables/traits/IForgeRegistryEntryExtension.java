package $group__.common.registrables.traits;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface IForgeRegistryEntryExtension<V extends IForgeRegistryEntry<V>> extends IForgeRegistryEntry<V> {
	V setUnlocalizedName(ResourceLocation name);

	default V setRegistryAndUnlocalizedName(String modId, String name) { return setRegistryAndUnlocalizedName(new ResourceLocation(modId, name)); }

	default V setRegistryAndUnlocalizedName(ResourceLocation name) { return setUnlocalizedName(name).setRegistryName(name); }
}
