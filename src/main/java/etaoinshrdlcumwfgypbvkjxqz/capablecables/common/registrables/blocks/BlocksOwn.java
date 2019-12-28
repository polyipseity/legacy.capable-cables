package etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrables.blocks;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrables.Registrables;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrables.blocks.templates.BlockDefault;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrables.creativetabs.CreativeTabsOwn;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.Constants.MOD_ID;

@SuppressWarnings("unused")
public final class BlocksOwn extends Registrables<Block> {
    public BlocksOwn() { super(Block.class); }
    public static BlocksOwn getInstance() { return getInstance(BlocksOwn.class); }

    /**
     * {@inheritDoc}
     */
    @Override
    @SubscribeEvent
    public void register(RegistryEvent.Register<Block> e) { super.register(e); }

    // Block template
    @SuppressWarnings("unused")
    private static final Block TEMPLATE_ = new BlockDefault(new Material(MapColor.AIR), MapColor.AIR)
            // Below one is required.
            .setRegistryName(MOD_ID, "template_")
            // All below are optional.
            .setUnlocalizedName("template_")
            .setCreativeTab(CreativeTabs.MISC)
            .setHardness(1337) // Not needed with setBlockUnbreakable()
            .setBlockUnbreakable() // setHardness(-1.0F)
            .setResistance(9001) // Not less than the value in setHardness(float) * 5 / 3
            .setLightLevel(15)
            .setLightOpacity(7)
            // Subclass is probably needed for below properties.
            .setTickRandomly(true); // Overrides are required for properties unlisted here.

    public final Block cable = new BlockCable().setRegistryAndUnlocalizedName(MOD_ID, "cable").setUnlocalizedName("cable").setCreativeTab(CreativeTabsOwn.DEFAULT);
}
