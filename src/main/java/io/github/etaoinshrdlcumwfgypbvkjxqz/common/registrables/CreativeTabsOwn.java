package io.github.etaoinshrdlcumwfgypbvkjxqz.common.registrables;

import io.github.etaoinshrdlcumwfgypbvkjxqz.common.registrables.items.ItemsOwn;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.utilities.Constants.MOD_ID;

public enum CreativeTabsOwn {
    ;
    public static final CreativeTabs DEFAULT = new net.minecraft.creativetab.CreativeTabs(MOD_ID) {
        @Override
        @SideOnly(Side.CLIENT)
        public ItemStack getTabIconItem() {
            return new ItemStack(ItemsOwn.getInstance().WRENCH);
        }
    };
}
