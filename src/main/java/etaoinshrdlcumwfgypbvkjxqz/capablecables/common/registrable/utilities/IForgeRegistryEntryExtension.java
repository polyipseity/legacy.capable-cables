package etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.utilities;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface IForgeRegistryEntryExtension<V extends IForgeRegistryEntry<V>> extends IForgeRegistryEntry<V> {
    V setUnlocalizedName(ResourceLocation name);

    V setRegistryAndUnlocalizedName(ResourceLocation name);
    default V setRegistryAndUnlocalizedName(String modID, String name) { return setRegistryAndUnlocalizedName(new ResourceLocation(modID, name)); }
}
