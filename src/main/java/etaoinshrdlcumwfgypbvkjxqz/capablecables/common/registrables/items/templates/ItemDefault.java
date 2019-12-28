package etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrables.items.templates;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrables.utilities.IForgeRegistryEntryExtension;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrables.utilities.RegistryHelper.getNamespacedUnlocalizedNameForRegistry;

public class ItemDefault extends Item implements IForgeRegistryEntryExtension<Item> {
    public ItemDefault() { super(); }

    /* IForgeRegistryEntryExtension */
    /**
     * {@inheritDoc}
     */
    @Override
    public Item setUnlocalizedName(ResourceLocation name) { return setUnlocalizedName(getNamespacedUnlocalizedNameForRegistry(name)); }
    /**
     * {@inheritDoc}
     */
    @Override
    public Item setRegistryAndUnlocalizedName(ResourceLocation name) { return setUnlocalizedName(name).setRegistryName(name); }
}
