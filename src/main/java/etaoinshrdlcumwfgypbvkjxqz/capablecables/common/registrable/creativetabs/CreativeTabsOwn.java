package etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.creativetabs;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.creativetabs.templates.CreativeTabsDefault;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.items.ItemsOwn;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.function.Function;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.Constants.MOD_ID;

public enum CreativeTabsOwn {
    ;
    // Creative tab template
    // Using a function here to avoid registering the tab.
    @SuppressWarnings("unused")
    private static final Function<CreativeTabsOwn, CreativeTabs> TEMPLATE_ = t -> new CreativeTabsDefault("template_") {
         /** {@inheritDoc} */
        @Override
        @SideOnly(Side.CLIENT) // IMPORTANT!
        public ItemStack getTabIconItem() { return new ItemStack(Items.AIR); }
        // See superclass for optional overrides.
    }.setNoTitle()
            .setNoScrollbar()
            .setRelevantEnchantmentTypes(EnumEnchantmentType.ALL)
            .setBackgroundImageName("template_");

    public static final CreativeTabs DEFAULT = new CreativeTabsDefault(MOD_ID) {
         /** {@inheritDoc} */
        @Override
        @SideOnly(Side.CLIENT)
        public ItemStack getTabIconItem() {
            return new ItemStack(ItemsOwn.getInstance().wrench);
        }
    };
}
