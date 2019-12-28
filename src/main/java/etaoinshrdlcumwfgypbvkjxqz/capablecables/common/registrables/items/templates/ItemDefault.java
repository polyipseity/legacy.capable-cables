package etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrables.items.templates;

import net.minecraft.item.Item;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrables.utilities.RegistryHelper.getNamespacedUnlocalizedNameForRegistry;

public class ItemDefault extends Item {
    /**
     * {@inheritDoc}
     */
    @Override
    public Item setUnlocalizedName(String unlocalizedName) { return super.setUnlocalizedName(getNamespacedUnlocalizedNameForRegistry(unlocalizedName)); }
}
