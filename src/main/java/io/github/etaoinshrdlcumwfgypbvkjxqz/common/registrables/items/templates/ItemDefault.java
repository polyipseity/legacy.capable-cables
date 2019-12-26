package io.github.etaoinshrdlcumwfgypbvkjxqz.common.registrables.items.templates;

import net.minecraft.item.Item;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.common.registrables.RegistryHelper.getNamespacedUnlocalizedNameForRegistry;

public class ItemDefault extends Item {
    @Override
    public Item setUnlocalizedName(String unlocalizedName) { return super.setUnlocalizedName(getNamespacedUnlocalizedNameForRegistry(unlocalizedName)); }
}
