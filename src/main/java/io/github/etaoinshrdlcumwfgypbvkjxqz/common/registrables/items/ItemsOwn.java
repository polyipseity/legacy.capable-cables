package io.github.etaoinshrdlcumwfgypbvkjxqz.common.registrables.items;

import io.github.etaoinshrdlcumwfgypbvkjxqz.common.registrables.Registrables;
import io.github.etaoinshrdlcumwfgypbvkjxqz.common.registrables.creativetabs.CreativeTabsOwn;
import io.github.etaoinshrdlcumwfgypbvkjxqz.common.registrables.items.templates.ItemDefault;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.utilities.Constants.MOD_ID;

@SuppressWarnings("unused")
public final class ItemsOwn extends Registrables<Item> {
    public ItemsOwn() { super(Item.class); }
    public static ItemsOwn getInstance() { return getInstance(ItemsOwn.class); }

    @Override
    @SubscribeEvent
    public void register(RegistryEvent.Register<Item> e) { super.register(e); }

    // Item template
    @SuppressWarnings("unused")
    private static final Item TEMPLATE_ = new ItemDefault()
            // Below one is required.
            .setRegistryName(MOD_ID, "template_")
            // All below are optional.
            .setUnlocalizedName("template_")
            .setCreativeTab(CreativeTabs.MISC)
            .setMaxStackSize(69)
            .setFull3D()
            // Subclass is probably needed for below properties (including the static initializer).
            .setHasSubtypes(true)
            .setMaxDamage(1337)
            .setNoRepair()
            .setContainerItem(Items.BUCKET);
    static { TEMPLATE_.setHarvestLevel("template_", 9001); } // Overrides are required for properties unlisted here.

    public final Item WRENCH = new ItemWrench().setRegistryName(MOD_ID, "wrench").setUnlocalizedName("wrench").setCreativeTab(CreativeTabsOwn.DEFAULT);
}
