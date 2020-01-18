package $group__.$modId__.common.registrable.blocks;

import $group__.$modId__.common.registrable.Registrable;
import $group__.$modId__.common.registrable.blocks.templates.BlockDefault;
import $group__.$modId__.common.registrable.creativetabs.CreativeTabsOwn;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static $group__.$modId__.utilities.variables.Constants.MOD_ID;

public final class BlocksOwn extends Registrable<Block> {
	/* SECTION variables */

	@SuppressWarnings("unused")
	public final Block cable = new BlockCable().setRegistryAndUnlocalizedName(MOD_ID, "cable").setUnlocalizedName("cable").setCreativeTab(CreativeTabsOwn.DEFAULT);


	/* SECTION methods */

	/** {@inheritDoc} */
	@Override
	@SubscribeEvent
	public void register(RegistryEvent.Register<Block> e) { super.register(e); }


	/* SECTION static variables */

	public static final BlocksOwn INSTANCE = getInstance(BlocksOwn.class);


	/* REFERENCE block template */

	@SuppressWarnings("unused")
	private static final Block TEMPLATE_ = new BlockDefault(new Material(MapColor.AIR), MapColor.AIR)
			// COMMENT below one is required
			.setRegistryName(MOD_ID, "template_")
			// COMMENT all below are optional
			.setUnlocalizedName("template_")
			.setCreativeTab(CreativeTabs.MISC)
			.setHardness(1337) // COMMENT NOT needed with setBlockUnbreakable()
			.setBlockUnbreakable() // COMMENT same as CODE{setHardness(-1.0F)}
			.setResistance(9001) // COMMENT NOT less than CODE{blockResistance * 5 / 3}
			.setLightLevel(15)
			.setLightOpacity(7)
			// COMMENT subclass is probably needed for below properties
			.setTickRandomly(true); // COMMENT overrides are required for properties unlisted here
}
