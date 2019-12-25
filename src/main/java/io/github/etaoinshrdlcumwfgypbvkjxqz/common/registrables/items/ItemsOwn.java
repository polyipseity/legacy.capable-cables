package io.github.etaoinshrdlcumwfgypbvkjxqz.common.registrables.items;

import io.github.etaoinshrdlcumwfgypbvkjxqz.common.registrables.CreativeTabsOwn;
import io.github.etaoinshrdlcumwfgypbvkjxqz.common.registrables.Registrables;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.utilities.Constants.MOD_ID;
import static io.github.etaoinshrdlcumwfgypbvkjxqz.utilities.RegistryHelper.getNamespacedUnlocalizedNameForRegistry;

@Mod.EventBusSubscriber(modid = MOD_ID)
public final class ItemsOwn extends Registrables<Item> {
    public ItemsOwn() { super(Item.class); }
    public static ItemsOwn getInstance() { return getInstance(ItemsOwn.class); }

    // Item template
    private static final Item TEMPLATE_ = new Item()
            .setRegistryName(MOD_ID, "template_")
            .setUnlocalizedName(getNamespacedUnlocalizedNameForRegistry("template_"))
            .setCreativeTab(CreativeTabs.MISC)
            .setMaxStackSize(69)
            .setFull3D()
            // Subclass is probably needed for below properties (including the initializer).
            .setHasSubtypes(true)
            .setMaxDamage(1337)
            .setNoRepair()
            .setContainerItem(Items.BUCKET);
    static {
        TEMPLATE_.setHarvestLevel("template_", 9001);
    }

    public final Item WRENCH = new ItemWrench().setRegistryName(MOD_ID, "wrench").setUnlocalizedName(getNamespacedUnlocalizedNameForRegistry("wrench")).setCreativeTab(CreativeTabsOwn.DEFAULT).setMaxStackSize(1);

    @Override
    @SubscribeEvent
    public void register(RegistryEvent.Register<Item> e) {
        super.register(e);
    }
}
