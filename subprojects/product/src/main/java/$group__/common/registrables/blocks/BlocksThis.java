package $group__.common.registrables.blocks;

import $group__.client.gui.debug.GuiComponentDebug;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static $group__.Constants.MOD_ID;

public enum BlocksThis {
	;

	public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, MOD_ID);

	public static final RegistryObject<Block> CABLE = BLOCKS.register("cable", BlockCable::new);

	// todo add debug flag
	@SuppressWarnings("unused")
	private static final RegistryObject<Block> DEBUG_GUI_COMPONENT = BLOCKS.register(GuiComponentDebug.PATH, GuiComponentDebug::getBlockEntry);
}
